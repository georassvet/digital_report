package ru.riji.comparator.services;

import com.ruiyun.jvppeteer.api.core.Browser;
import com.ruiyun.jvppeteer.cdp.core.Puppeteer;
import com.ruiyun.jvppeteer.cdp.core.Tracing;
import com.ruiyun.jvppeteer.cdp.entities.LaunchOptions;
import com.ruiyun.jvppeteer.api.core.Page;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class DevToolService {

    public static final LaunchOptions launchOptions = LaunchOptions.builder().
//            executablePath("C:\\Users\\fanyong\\Desktop\\typescriptPri\\.local-browser\\chrome-win32\\chrome-win32\\chrome.exe").
//            executablePath("C:\\Users\\fanyong\\Desktop\\typescriptPri\\.local-browser\\chrome-win32\\chrome-win32\\chrome.exe").product(Product.Chrome).
//        executablePath("C:\\Users\\fanyong\\Desktop\\jvppeteer\\example\\.local-browser\\win32-133.0\\core\\firefox.exe").
//        product(Product.Firefox).
        headless(false).
//            protocol(Protocol.CDP).
            //不设置窗口大小
                    defaultViewport(null).
            build();
    public static void main(String[] args) {
        Browser cdpBrowser = null;
        try {
            cdpBrowser = Puppeteer.launch(launchOptions);
            Page page = cdpBrowser.newPage();
            Set<String> categories = new HashSet<>();
            categories.add("devtools.timeline");
           page.tracing().start("./tracing.json", false,  categories);
           page.goTo("https://gosuslugi.ru");
            Tracing tracing = page.tracing();
           page.tracing().stop();

            System.out.println(tracing);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
