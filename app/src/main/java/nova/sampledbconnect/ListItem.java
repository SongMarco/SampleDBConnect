package nova.sampledbconnect;

/**
 * Created by Administrator on 2017-11-16.
 */

public class ListItem {

    private String[] mData;

    public ListItem(String[] data ){


        mData = data;
    }

    public ListItem(String email, String password, String name, String birthday){

        mData = new String[4];
        mData[0] = email;
        mData[1] = password;
        mData[2] = name;
        mData[3] = birthday;

    }

    public String[] getData(){
        return mData;
    }

    public String getData(int index){
        return mData[index];
    }

    public void setData(String[] data){
        mData = data;
    }



}


