package crawl.spider.scheduler;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by BFD_303 on 2016/12/14.
 */
public class DuplicateSet {

    private Set<String> dupSet = new HashSet<>();

    public void addToDupSet(String... urls){
        Arrays.stream(urls).forEach(u -> dupSet.add(u));
    }

    public void addToDupSet(Collection<String> urls){
        urls.forEach(u -> dupSet.add(u));
    }

    public boolean idDuplied(String url){
        return dupSet.contains(url);
    }

    public int dupSize(){
        return dupSet.size();
    }

}
