package com.example.syy.controls;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class control1 {
    //用户信息 注册 文件上传
    @RequestMapping("/userReg2.html")
    String userReg2()
    {return "userReg2.html";  }
    //用户 登录
    @RequestMapping("/Login.html")
    String Login()
    {return "Login.html";  }


    //我的资料
    @RequestMapping("/MyInfo.html")
    String MyInfo()
    {return "MyInfo";  }
    //我的xxx
    @RequestMapping("/My.html")
    String My()
    {return "My.html";  }
    //我的空间
    @RequestMapping("/MySpace.html")
    String MySpace()
    {return "MySpace.html";  }



    //达人认证
    @RequestMapping("/Certification.html")
    String Certification()
    {return "Certification.html";  }

    //管理员管理用户
    @RequestMapping("/Mana.html")
    String Mana()
    {return "Mana.html";  }

    //登录成功进入主页
    @RequestMapping("/Index.html")
    String Index()
    {return "Index.html";  }
    //发布活动
    @RequestMapping("/pubAct.html")
    String pubAct()
    {return "pubAct.html";  }
    //修改活动
    @RequestMapping("/ModiAct.html")
    String ModiAct()
    {return "ModiAct.html";  }
    //活动详情
    @RequestMapping("/ActivityDetails.html")
    String ActivityDetails()
    {return "ActivityDetails.html";  }
    //发布动态
    @RequestMapping("/PubDynamic.html")
    String PubDynamic()
    {return "PubDynamic.html";  }
    //发布动态
    @RequestMapping("/pubDynamic1.html")
    String pubDynamic1()
    {return "pubDynamic1.html";  }
    //发现页-动态查看
    @RequestMapping("/Discover3.html")
    String Discover3()
    {return "Discover3.html";  }

    @RequestMapping("/Discover4.html")
    String Discover4()
    {return "Discover4.html";  }
    //发现页-活动查看
    @RequestMapping("/AllActivities.html")
    String AllActivities()
    {return "AllActivities.html";}
    //动态详情
    @RequestMapping("/dynDetails.html")
    String dynDetails()
    {return "dynDetails.html";  }
    @RequestMapping("/dynDetails1.html")
    String dynDetails1()
    {return "dynDetails1.html";  }
   //活动报名
    @RequestMapping("/Signup.html")
    String Signup(){
        return "Signup.html";
    }



    @RequestMapping("/MyCollects.html")
    String MyCollects(){
        return "MyCollects.html";
    }
    //我的消息
    @RequestMapping("/myMessages.html")
    String myMessages(){
        return "myMessages.html";
    }

    //审核
    @RequestMapping("/Audit.html")
    String Audit()
    {return "Audit.html";}
    //审核活动
    @RequestMapping("/ActivityDetails0.html")
    String ActivityDetails0()
    {return "ActivityDetails0.html";  }
  //我参加的活动
    @RequestMapping("/MyActivities.html")
    String MyActivities()
    {return "MyActivities.html";}

    //我发布的动态
    @RequestMapping("/MyDynamic.html")
    String MyDynamic()
    {return "MyDynamic.html";}
    //用户账号管理
    @RequestMapping("/UserAudit.html")
    String UserAudit()
    {return "UserAudit.html";}
    //动态审核详情
    @RequestMapping("/DynamicDetails.html")
    String DynamicDetails()
    {return "DynamicDetails.html";}
}
