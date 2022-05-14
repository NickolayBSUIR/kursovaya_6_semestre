package thesis.webcryptoexchange.pattern;

import org.json.simple.*;

public class JSONCollection {
    
    private JSONArray json;

    public JSONCollection(JSONArray json) {
        this.json = json;
    }

    public Iterator iterator(){
        return new JSONIterator();
    }

    private class JSONIterator implements Iterator{

        private int index = 0;

        public JSONObject next(){
            JSONObject obj = (JSONObject)json.get(index);
            index++;
            return obj;
        }

        public boolean hasNext(){
            return index < json.size();
        }
    }

}
