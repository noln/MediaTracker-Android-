package model.jsonreaders.mal;



import androidx.annotation.NonNull;

import java.util.Iterator;
import java.util.List;

public class MalResults implements Iterable<MalMediaItem> {

    private List<MalMediaItem> results;

    @NonNull
    @Override
    public Iterator<MalMediaItem> iterator() {
        return results.iterator();
    }

    //This will actually never be instantiated
}
