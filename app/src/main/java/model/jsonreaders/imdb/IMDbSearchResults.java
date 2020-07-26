package model.jsonreaders.imdb;

import java.util.Iterator;
import java.util.List;

// Represent a (movie, animation...) cited from outside sources like i m d b will develop after http request is
// implemented
public class IMDbSearchResults implements Iterable<IMDbSearchMediaItem> {

    private List<IMDbSearchMediaItem> imDbResults;

    public List<IMDbSearchMediaItem> getImDbResults() {
        return imDbResults;
    }

    @Override
    public Iterator<IMDbSearchMediaItem> iterator() {
        return imDbResults.iterator();
    }
}
