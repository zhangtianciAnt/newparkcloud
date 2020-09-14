package com.nt.controller.Controller.BASF.BASFLANController;

import com.alibaba.fastjson.JSONObject;
import com.nt.controller.Config.BASF.MultiThreadScheduleTask;
import com.nt.controller.Controller.WebSocket.WebSocket;
import com.nt.dao_BASF.*;
import com.nt.dao_BASF.VO.PimsVo;
import com.nt.service_BASF.mapper.PimsAlarmDetailMapper;
import com.nt.service_BASF.mapper.PimsAlarmMapper;
import com.nt.service_BASF.mapper.PimsPointMapper;
import com.nt.service_BASF.mapper.PimsdataMapper;
import com.nt.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.TextMessage;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Classname BASFTestController
 * @Description PIMS接口类
 * @Date 2020/8/31 14:32
 * @Author skaixx
 */
@RestController
@RequestMapping("/pims")
public class BASFPIMSController {

    @Autowired
    private PimsPointMapper pimsPointMapper;

    @Autowired
    private PimsdataMapper pimsdataMapper;

    @Resource
    private PimsAlarmMapper pimsAlarmMapper;

    @Resource
    private PimsAlarmDetailMapper pimsAlarmDetailMapper;

    /**
     * 获取opc正常数据
     *
     * @param data
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getData", method = {RequestMethod.POST})
    public ApiResult getData(@RequestBody List<Object> data, HttpServletRequest request) throws Exception {
        // 用于 推送大屏socket
        List<PimsVo> pimsVoList = new ArrayList<>();
        // 用于 数据插入数据库
        List<Pimsdata> pimsdataList = new ArrayList<>();
        // 用于 根据opc传来的信息查询点位
        List<Pimspoint> pimspoints = new ArrayList<>();
        // 用于 查询条件 key：name value:value
        Map<String, String> pimspointMap = new HashMap<String, String>();
        // 根据名称获取点位信息
        Pimspoint pimspoint = null;
        for (Object info : data) {
            String name = ((LinkedHashMap) info).get("key").toString();
            String value = ((LinkedHashMap) info).get("value").toString();
            pimspoint = new Pimspoint();
            pimspoint.setPimspointname(name);
            pimspoints.add(pimspoint);
            pimspointMap.put(name, value);
        }
        pimspoints = pimsPointMapper.getPimsPoint(pimspointMap);
        // 插入opc传来的数据
        Pimsdata pimsdata = null;
        for (Pimspoint p : pimspoints) {
            pimsdata = new Pimsdata();
            pimsdata.setMonitoringpoint(p.getId());
            pimsdata.setPimsid(UUID.randomUUID().toString());
            pimsdata.setPimsdata(BigDecimal.valueOf
                    (Double.parseDouble(
                            pimspointMap.get(p.getPimspointname()))
                    )
            );
            pimsdata.preInsert();
            p.setPimsdata(pimsdata);
            pimsdataList.add(pimsdata);

        }
        if (pimsdataList.size() > 0) {
            pimsdataMapper.insertPimsDataList(pimsdataList);
            MultiThreadScheduleTask.webSocketVo.setPimspoints(pimspoints);
            WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(MultiThreadScheduleTask.webSocketVo)));
        }
        return ApiResult.success();
    }

    /**
     * 接收opc报警信息
     *
     * @param data
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getAlarm", method = {RequestMethod.POST})
    public ApiResult getAlarm(@RequestBody List<Object> data) throws Exception {
        String name = ((LinkedHashMap) data.get(0)).get("key").toString();
        String value = ((LinkedHashMap) data.get(0)).get("value").toString();
        String status = "";
        // 根据点位名称，查询点位信息
        String point = name.substring(1);
        Pimspoint pimspoint = new Pimspoint();
        pimspoint.setPimspointname(point);
        pimspoint = pimsPointMapper.selectOne(pimspoint);
        if (pimspoint == null) {
            return ApiResult.fail("未找到该点位信息。" + name);
        }
        // 根据点位名称，获取报警单。
        Pimsalarm pimsalarm = new Pimsalarm();
        pimsalarm.setPimspointid(pimspoint.getId());
        pimsalarm = pimsAlarmMapper.selectOne(pimsalarm);
        if ("1".equals(value)) {
            // 报警，生成报警单，推送socket
            // 截取点位第一位字符，用于判断报警类型
            status = PimsAlarmStatus.getStatus(name.substring(0, 1));
            // 判断报警单是否存在
            if (pimsalarm == null) {
                // 先生成报警单
                pimsalarm = new Pimsalarm(UUID.randomUUID().toString(), pimspoint.getId(), status);
                pimsalarm.preInsert();
                pimsAlarmMapper.insert(pimsalarm);
            } else {
                // 更新报警单
                pimsalarm.preUpdate();
                pimsalarm.setAlarm(status);
                pimsAlarmMapper.updateByPrimaryKeySelective(pimsalarm);
            }
            // 插入明细
            Pimsalarmdetail pimsalarmdetail = new Pimsalarmdetail(UUID.randomUUID().toString(), pimsalarm.getId(), status, new Date());
            pimsAlarmDetailMapper.insert(pimsalarmdetail);
            // 设置推送socket信息
            MultiThreadScheduleTask.webSocketVo.setPimsalarm(pimsalarm);
        } else {
            status = PimsAlarmStatus.getStatus("4");
            // 判断报警单是否存在
            if (pimsalarm != null) {
                // 修改报警单-当前报警状态字断
                pimsalarm.setAlarm(status);
                pimsalarm.preUpdate();
                pimsAlarmMapper.updateByPrimaryKeySelective(pimsalarm);
                // 设置推送socket信息
                MultiThreadScheduleTask.webSocketVo.setPimsalarm(pimsalarm);
            }
        }
        MultiThreadScheduleTask.webSocketVo.setPimsalarmList(pimsAlarmMapper.getAllPimsAlarm("1"));
        // 推送
        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(MultiThreadScheduleTask.webSocketVo)));
        return ApiResult.success();
    }

    /**
     * 用于大屏初始化获取所有点位最新的数据
     *
     * @return
     */
    @RequestMapping(value = "getAllPimsPointData", method = {RequestMethod.GET})
    public ApiResult getAllPimsPointData() {
        return ApiResult.success(pimsPointMapper.getAllPimsPointData());
    }

    /**
     * 获取报警单及报警详情
     *
     * @param type 0:查询所有，1：查询正在报警的
     * @return
     */
    @RequestMapping(value = "getAllPimsAlarm", method = {RequestMethod.GET})
    public ApiResult getAllPimsAlarm(@RequestParam String type) {
        List<Pimsalarm> pimsalarmList = pimsAlarmMapper.getAllPimsAlarm(type);
        MultiThreadScheduleTask.webSocketVo.setPimsalarmList(pimsalarmList);
        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(MultiThreadScheduleTask.webSocketVo)));
        return ApiResult.success();
    }
}
