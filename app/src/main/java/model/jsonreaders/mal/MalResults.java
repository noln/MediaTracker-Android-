package model.jsonreaders.mal;



import java.util.Iterator;
import java.util.List;

public class MalResults implements Iterable<MalResults> {

    private List<MalResults> results;

    //This will actually never be instantiated

    @Override
    public Iterator<MalResults> iterator() {
        return results.iterator();
    }


}
