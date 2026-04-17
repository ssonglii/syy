package com.example.syy.controls;

import com.example.syy.annotation.PassToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@CrossOrigin
public class controlCookieSesson {
    @RequestMapping("/bbs/getSession")//这是在bbs路径下发出的请求，一定要搞清楚路径,空
    @PassToken
    public String getSession(@RequestParam("key") String key, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session1 = request.getSession();
      String value0="游客";
      System.out.print("session的:"+key);
        if (session1 != null) {
            if (session1.getAttribute(key)!= null)
            {
                value0 = session1.getAttribute(key).toString().trim();
           }

        }
        //测试cookies
        Cookie[] cookies=request.getCookies();
        System.out.print("cookie 大小:"+cookies.length);
        String c=" ";
        for(int i=0;i<cookies.length;i++)
        {
            if(cookies[i].getName().equals(key))
            {c=cookies[i].getValue();break;}
        }
           // System.out.print(cookies[0].getName()+" :"+cookies[0].getValue());

        return value0+c;  //通过sesson,cookie,返回用户名
    }
}
