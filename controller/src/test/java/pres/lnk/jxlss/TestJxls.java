package pres.lnk.jxlss;

import com.nt.utils.jxlsUtil.JxlsBuilder;
import com.nt.utils.jxlsUtil.JxlsImage;
import com.nt.utils.jxlsUtil.JxlsUtil;
import org.junit.Test;
import pres.lnk.jxlss.demo.Employee;
import pres.lnk.jxlss.demo.Experience;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 测试生成excel文件
 *
 * @Author lnk
 * @Date 2018/1/25
 */
public class TestJxls {

    @Test
    public void testJxls() throws Exception {
        String outPath = "D:/out_employee.xlsx";
        String imgRoot = TestJxls.class.getClassLoader().getResource("jxls_templates").getPath();
        Employee emp = getEmployee();
        List<Experience> educationList = getEducationList();
        List<Experience> workList = getWorkList();
        JxlsBuilder jxlsBuilder = JxlsBuilder
                /* -- 加载模板方式 -- */
                //使用文件流加载模板
//                .getBuilder(inputStream)
                //使用文件加载模板
//                .getBuilder(file)
                //使用路径加载模板，可以是相对路径，也可以绝对路径
                .getBuilder("xlsx/employee.xlsx")

                /* -- 输出文件方式 -- */
                //指定输出的文件流
//                .out(outputStream)
                //指定输出的文件
//                .out(file)
                //指定输出的路径
                .out(outPath)

                /* 添加生成的数据 */
                .putVar("emp", emp)
                .putVar("educationList", educationList)
                .putVar("workList", workList)
                //设置图片路径的根目录
                .imageRoot(imgRoot)
                //设置如果图片缺失不终止生成
                .ignoreImageMiss(true)
                //添加自定工具类
//                .addFunction("jx", new JxlsUtil())
                .build();
        System.out.println("导出成功");
        System.out.println(jxlsBuilder.getOutFile().getAbsolutePath());
    }

    private static Employee getEmployee() {
        return new Employee();
    }

    private static List<Experience> getEducationList() {
        List<Experience> list = new ArrayList<>();
        list.add(new Experience("xxx大学"));
        list.add(new Experience("xxx高中"));
        list.add(new Experience("xxx初中"));
        return list;
    }


    private static List<Experience> getWorkList() {
        List<Experience> list = new ArrayList<>();
//        list.add(new Experience("广州科腾信息技术有限公司"));
        return list;
    }
}
