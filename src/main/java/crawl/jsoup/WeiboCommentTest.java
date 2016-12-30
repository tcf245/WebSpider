package crawl.jsoup;

import org.apache.commons.io.FileUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by BFD_303 on 2016/12/26.
 */
public class WeiboCommentTest {

    public static void main(String[] args) {
        try {
            String html = FileUtils.readFileToString(new File("etc/comments.html"));

            HtmlCleaner cleaner = new HtmlCleaner();
            TagNode root = cleaner.clean(html);

            Object[] divnews = root.evaluateXPath("//div[@class='list_li S_line1 clearfix']");

            if(divnews.length>0){
                for(int i=0;i<divnews.length;i++){
                    Map<String, Object> map = new HashMap<String, Object>();
                    TagNode div = (TagNode)divnews[i];


                    if(div.evaluateXPath("//div[@class='WB_face W_fl']").length == 0) continue;

                    int replycnt = 0,upcnt = 0;
                    String text = div.getText().toString();
                    //解析回复的点赞数
                    Object[] upObj = div.evaluateXPath("//div[@class='list_con']/div[@class='WB_func clearfix']/div[1]/ul/li[3]/span/a/span/em[2]");
                    {
                        if (upObj.length > 0){
                            String upStr = ((TagNode)upObj[0]).getText().toString();
                            upcnt = upStr.equals("赞") ? 0 : Integer.parseInt(upStr);
                        }
                    }
                    //解析回复的回复数
                    Object[] replyObj = div.evaluateXPath("//div[@class='list_con']/div[@class='list_box_in S_bg3']");
                    {
                        if (replyObj.length == 2){
                            //找到回复数并匹配
                            TagNode reply = (TagNode) replyObj[1];
                            System.out.println("reply text : " + reply.getText().toString());
                            replycnt = getReplyCount(reply.getText().toString());

                        }else if (replyObj.length == 1){
                            TagNode reply = (TagNode) replyObj[0];
                            replyObj = reply.evaluateXPath("//div[@class='list_li_v2']/div[@class='WB_text']");
                            if (replyObj.length == 1){
                                //todo 获取回复数
                                replycnt = getReplyCount(((TagNode)replyObj[0]).getText().toString());

                            }else{
                                replyObj = reply.evaluateXPath("//div[@class='list_ul']/div[@class='list_li S_line1 clearfix']");
                                {
                                    // 没有回帖数的情况下 根据标签数量统计
                                    replycnt = replyObj.length;
                                }
                            }
                        }
                    }
                    //todo output
                    System.out.println("get reply content is --> " + text);
                    System.out.println("get reply count is --> " + replycnt);
                    System.out.println("get up count is --> " + upcnt);
                    System.out.println("===========");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XPatherException e) {
            e.printStackTrace();
        }
    }


    public void getReplyAndUpCnt(TagNode div,Map<String,Object> map) throws XPatherException {
        if(div.evaluateXPath("//div[@class='WB_face W_fl']").length == 0) return;

        int replycnt = 0,upcnt = 0;
        String text = div.getText().toString();
        //解析回复的点赞数
        Object[] upObj = div.evaluateXPath("//div[@class='list_con']/div[@class='WB_func clearfix']/div[1]/ul/li[3]/span/a/span/em[2]");
        {
            if (upObj.length > 0){
                String upStr = ((TagNode)upObj[0]).getText().toString();
                upcnt = upStr.equals("赞") ? 0 : Integer.parseInt(upStr);
            }
        }
        //解析回复的回复数
        Object[] replyObj = div.evaluateXPath("//div[@class='list_con']/div[@class='list_box_in S_bg3']");
        {
            if (replyObj.length == 2){
                //找到回复数并匹配
                TagNode reply = (TagNode) replyObj[1];
                System.out.println("reply text : " + reply.getText().toString());
                replycnt = getReplyCount(reply.getText().toString());

            }else if (replyObj.length == 1){
                TagNode reply = (TagNode) replyObj[0];
                replyObj = reply.evaluateXPath("//div[@class='list_li_v2']/div[@class='WB_text']");
                if (replyObj.length == 1){
                    //todo 获取回复数
                    replycnt = getReplyCount(((TagNode)replyObj[0]).getText().toString());

                }else{
                    replyObj = reply.evaluateXPath("//div[@class='list_ul']/div[@class='list_li S_line1 clearfix']");
                    {
                        // 没有回帖数的情况下 根据标签数量统计
                        replycnt = replyObj.length;
                    }
                }
            }
        }
        //todo output
        System.out.println("get reply content is --> " + text);
        System.out.println("get reply count is --> " + replycnt);
        System.out.println("get up count is --> " + upcnt);
        System.out.println("===========");
    }


    /**
     * 提取回复数信息, 数据格式异常返回0  示例数据 （共18条回复）
     * @param data
     * @return
     */
    public static int getReplyCount(String data){
        if (data == null) return 0;
        Matcher m = Pattern.compile("共(\\d+)条回复").matcher(data);
        if (m.find()){
            return Integer.parseInt(m.group(1));
        }
        return 0;
    }

    public static void parseWithJsoup() {
        try {
            String html = FileUtils.readFileToString(new File("etc/comment.html"));

            Document doc = Jsoup.parse(html);
            Elements comments = doc.select("div.list_li");
            comments.forEach(c -> {

                String username = c.select("a[usercard]").first().text();
                String content = c.select("div.WB_text").first().text();

                String replycnt = c.select("a[action-data]").text();
                if (replycnt.contains("回复 ñ赞")){
                    Elements replys = c.select("div.list_con");
                    replycnt = (replys.size() - 1) + "";
                }else{
                    Matcher m = Pattern.compile("共(\\d+)条回复").matcher(replycnt);
                    if (m.find())
                        replycnt = m.group(1);
                }
                String upcnt = c.select("span[node-type] em").get(1).text();
                upcnt = upcnt.equals("赞") ? 0 + "" : upcnt;

                System.out.println("username -> " + username);
                System.out.println("content -> " + content);
                System.out.println("replycnt -> " + (replycnt));
                System.out.println("upcnt -> " + upcnt);
                System.out.println("================");

            });


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
