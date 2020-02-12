package com.nt.service_pfans.PFANS5000.Impl;

import com.nt.dao_Pfans.PFANS5000.CloseApplicat;
import com.nt.dao_Pfans.PFANS5000.ProjectSecore;
import com.nt.dao_Pfans.PFANS5000.StageNews;
import com.nt.dao_Pfans.PFANS5000.Vo.CloseApplicatVo;
import com.nt.service_pfans.PFANS5000.CloseApplicatService;
import com.nt.service_pfans.PFANS5000.mapper.CloseApplicatMapper;
import com.nt.service_pfans.PFANS5000.mapper.ProjectSecoreMapper;
import com.nt.service_pfans.PFANS5000.mapper.StageNewsMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
@Transactional(rollbackFor = Exception.class)
public class CloseApplicatServiceImpl implements CloseApplicatService {

    @Autowired
    private CloseApplicatMapper closeApplicatMapper;

    @Autowired
    private ProjectSecoreMapper projectSecoreMapper;

    @Autowired
    private StageNewsMapper stageNewsMapper;

    @Override
    public void insert(CloseApplicatVo closeApplicatVo, TokenModel tokenModel) throws Exception {
        String closeapplicatid= UUID.randomUUID().toString();
        CloseApplicat closeApplicat=new CloseApplicat();
        BeanUtils.copyProperties(closeApplicatVo.getCloseApplicat(),closeApplicat);
        closeApplicat.preInsert(tokenModel);
        closeApplicat.setCloseapplicatid(closeapplicatid);
        closeApplicatMapper.insertSelective(closeApplicat);
        List<ProjectSecore> projectSecoreList=closeApplicatVo.getProjectSecore();
        List<StageNews> stageNewsList=closeApplicatVo.getStageNews();
        if(projectSecoreList!=null){
            int rowindex = 0;
            for(ProjectSecore projectSecore:projectSecoreList){
                rowindex = rowindex + 1;
                projectSecore.preInsert(tokenModel);
                projectSecore.setProjectsecoreid(UUID.randomUUID().toString());
                projectSecore.setCloseapplicatid(closeapplicatid);
                projectSecore.setRowindex(rowindex);
                projectSecoreMapper.insert(projectSecore);
            }
        }
        if (stageNewsList != null) {
            int rowindex = 0;
            for (StageNews stageNews : stageNewsList) {
                rowindex = rowindex + 1;
                stageNews.preInsert(tokenModel);
                stageNews.setStagenews_id(UUID.randomUUID().toString());
                stageNews.setCloseapplicatid(closeapplicatid);
                stageNews.setRowindex(rowindex);
                stageNewsMapper.insertSelective(stageNews);
            }
        }
    }

    @Override
    public void update(CloseApplicatVo closeApplicatVo, TokenModel tokenModel) throws Exception {
        CloseApplicat closeApplicat=new CloseApplicat();
        BeanUtils.copyProperties(closeApplicatVo.getCloseApplicat(),closeApplicat);
        closeApplicat.preUpdate(tokenModel);
        closeApplicatMapper.updateByPrimaryKey(closeApplicat);
        String scloseapplicatid=closeApplicat.getCloseapplicatid();

        ProjectSecore pjs=new ProjectSecore();
        pjs.setCloseapplicatid(scloseapplicatid);
        projectSecoreMapper.delete(pjs);
        List<ProjectSecore> projectSecorelist=closeApplicatVo.getProjectSecore();

        StageNews sgn=new StageNews();
        sgn.setCloseapplicatid(scloseapplicatid);
        stageNewsMapper.delete(sgn);
        List<StageNews> stageNewslist=closeApplicatVo.getStageNews();

        if(projectSecorelist!=null){
            int rowindex=0;
            for(ProjectSecore projectSecore : projectSecorelist){
                rowindex=rowindex+1;
                projectSecore.preInsert(tokenModel);
                projectSecore.setProjectsecoreid(UUID.randomUUID().toString());
                projectSecore.setCloseapplicatid(scloseapplicatid);
                projectSecore.setRowindex(rowindex);
               projectSecoreMapper.insertSelective(projectSecore);
            }
        }
        if(stageNewslist!=null){
            int rowindex=0;
            for(StageNews stageNews : stageNewslist){
                rowindex=rowindex+1;
                stageNews.preInsert(tokenModel);
                stageNews.setStagenews_id(UUID.randomUUID().toString());
                stageNews.setCloseapplicatid(scloseapplicatid);
                stageNews.setRowindex(rowindex);
                stageNewsMapper.insertSelective(stageNews);
            }
        }
    }

    @Override
    public List<CloseApplicat> get(CloseApplicat closeApplicat) throws Exception {
        return closeApplicatMapper.select(closeApplicat);
    }

    @Override
    public CloseApplicatVo selectById(String closeApplicatid) throws Exception {
       CloseApplicatVo cloVo=new CloseApplicatVo();
       ProjectSecore secore=new ProjectSecore();
       StageNews news=new StageNews();
       secore.setCloseapplicatid(closeApplicatid);
       news.setCloseapplicatid(closeApplicatid);
       List<ProjectSecore> projectSecoreList=projectSecoreMapper.select(secore);
       List<StageNews> stageNewsList=stageNewsMapper.select(news);
        projectSecoreList=projectSecoreList.stream().sorted(Comparator.comparing(ProjectSecore::getRowindex)).collect(Collectors.toList());
        stageNewsList=stageNewsList.stream().sorted(Comparator.comparing(StageNews::getRowindex)).collect(Collectors.toList());
        CloseApplicat cea=closeApplicatMapper.selectByPrimaryKey(closeApplicatid);
        cloVo.setCloseApplicat(cea);
        cloVo.setProjectSecore(projectSecoreList);
        cloVo.setStageNews(stageNewsList);
        return cloVo;
    }
}
