package com.example.syy.utils;

import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.UUID;
@Component
public class MyTools {
  /*  @Value("${upload.path}")//从系统配置文件中直接取文件路径,以下的方法都共用它*/
    private String uploadPath1="D:\\A_JAVA3\\idea1\\SYY\\src\\main\\resources\\static\\image\\upload\\";


    public void  requestDataAandFilesToModel(HttpServletRequest request, Object model0) throws IOException, ServletException, NoSuchFieldException, IllegalAccessException, ParseException {

        Collection<Part> parts=request.getParts();
        boolean file0=false;
        Enumeration<String> values0=request.getParameterNames();//把所有的元素先在part循环之前取出来，不能放在for(Part part:parts)循环内部去了，否则多次重复
        String allFileName="";//如果有多个文件，文件名累加
        String fileNameInMode="";//保存实体模型中的文件列照片列的成员变量名，如photo列的名字
        for(Part part:parts) //去遍历每个表单控件部分
        {
            if (part.getContentType() == null)//非文件数据
            {
                while (values0.hasMoreElements())//把非文件数据遍历取出来
                {
                    String str0 = values0.nextElement();//数据项的name取出来
                    if (request.getParameterValues(str0).length > 1)  //前端多个数据项的name相同，如爱好，多文件同一个name
                    {
                        String muliName = ""; //累加同name的复选框数据
                        for (int i = 0; i < request.getParameterValues(str0).length; i++) {
                            muliName = muliName + request.getParameterValues(str0)[i] + "-";
                        }
                        muliName = muliName.substring(0, muliName.length() - 1);//去掉多子项最后“-”
                        //反射到实体中去
                        Field f0 = null;//用于反射取得实体的对应成员
                        f0 = model0.getClass().getDeclaredField(str0);
                        f0.setAccessible(true);//可反射操作实体私有成员
                        f0.set(model0, muliName);//把多个同name的参数写到实体成员中
                    } else //处理非同name的前端数据到实体
                    {
                        Field f0 = null;
                        f0 = model0.getClass().getDeclaredField(str0);
                        f0.setAccessible(true);
                        //要判断取出的数据类型，字符串没问题，日期和数值可能会出错，映射到实体要报错
                        if (f0.getType() == Integer.class)//准备映射整数
                            f0.set(model0, Integer.parseInt(request.getParameter(str0)));
                    /*    tzInfo.setTZno(Integer.parseInt(request.get("TZno")));*/

                        else if (f0.getType() == Date.class) {//准备映射日期型，根据需要设置
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            f0.set(model0, df.parse(request.getParameter(str0)));
                        } else //不需要处理类型其它数据
                            f0.set(model0, request.getParameter(str0));
                    }
                }
            }

                else
            {
                String filename = part.getSubmittedFileName();
                //先要判断上传来的文件是否为空
                fileNameInMode = part.getName();//从part中取出该文件name（如name="photo")
                System.out.print("\n" + fileNameInMode + "\n");
                if (!(filename.equals(null) || filename.length() < 3)) {
                    System.out.print("\n这是前端过来的文件名字：" + filename + "\n");
                    filename = UUID.randomUUID().toString().substring(0,5) + filename;
                    String fullFilePath = uploadPath1 + filename; // 计算完整的文件路径
                    System.out.println("将要写入的完整文件路径: " + fullFilePath); // 打印完整文件路径
                    part.write(fullFilePath);
                   /* part.write(uploadPath + filename);*/
                    /* allFileName +=filename+"｜"  ;//注意 是一个全角的竖线，半角下这些标点尽量不用，否则可能被识别为转义或控制字符*/
                    // 拼接数据库中要存储的文件名（带上路径前缀）
                    String newFilename = "/static/image/upload/" + filename;
                    allFileName +=newFilename+"｜"  ;//注意 是一个全角的竖线，半角下这些标点尽量不用，否则可能被识别为转义或控制字符
                    allFileName=allFileName.substring(0, allFileName.length()-1);
                    System.out.print("\n这是前端过来的所有文件名字：" + allFileName + "\n");
                }
            }
        }
        //当所有part循环完了，再单独把若干个文件名累加后的结果映射到实体中去
        if(!(fileNameInMode.equals(null)||fileNameInMode=="")) {
            //如果界面里根本就没有文件照片的话，这个就不去映射了，否则出错
            Field f0 = null;
            f0 = model0.getClass().getDeclaredField(fileNameInMode);
            f0.setAccessible(true);
            f0.set(model0, allFileName);
        }
    }
}
