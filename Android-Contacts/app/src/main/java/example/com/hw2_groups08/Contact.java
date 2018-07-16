/**
 * Assignment: Homework 02
 * FileName: Contact.java
 * AuthorS: Vaijyant Tomar
 */

package example.com.hw2_groups08;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ScrollView;

import java.io.Serializable;
import java.util.ArrayList;


public class Contact implements Serializable{
    static ArrayList<Contact> contactList = new ArrayList<Contact>();

    transient Bitmap image;
    String  first,
            last,
            company,
            phone,
            email,
            url,
            address,
            birthday,
            nickname,
            facebookURL,
            twitterURL,
            skype,
            youTubeChannel;

    String ids = "";

    public Contact(Bitmap image, String first, String last, String company, String phone, String email,
                   String url, String address, String birthday, String nickname, String facebookURL,
                   String twitterURL, String skype, String youTubeChannel) {
        this.image = image;
        this.first = first;
        this.last = last;
        this.company = company;
        this.phone = phone;
        this.email = email;
        this.url = url;
        this.address = address;
        this.birthday = birthday;
        this.nickname = nickname;
        this.facebookURL = facebookURL;
        this.twitterURL = twitterURL;
        this.skype = skype;
        this.youTubeChannel = youTubeChannel;
    }

    public Bitmap getImage(){
        return image;
    }

    public String getFirst() {
        return first;
    }

    public String getLast() {
        return last;
    }

    public String getCompany() {
        return company;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getUrl() {
        return url;
    }

    public String getAddress() {
        return address;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getNickname() {
        return nickname;
    }

    public String getFacebookURL() {
        return facebookURL;
    }

    public String getTwitterURL() {
        return twitterURL;
    }

    public String getSkype() {
        return skype;
    }

    public String getYouTubeChannel() {
        return youTubeChannel;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setFacebookURL(String facebookURL) {
        this.facebookURL = facebookURL;
    }

    public void setTwitterURL(String twitterURL) {
        this.twitterURL = twitterURL;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public void setYouTubeChannel(String youTubeChannel) {
        this.youTubeChannel = youTubeChannel;
    }

    //Support methods
    public String getIds() {
        return ids;
    }
    public void setIdsEmpty() {
        this.ids = "";
    }

    public static Contact getContact(int anyId){
        for(Contact contact: contactList) {
            String idList[] = contact.getIds().split(",");
            for(String id : idList){
                if(id.equals(anyId+""))
                    return contact;
            }
        }
        return null;
    }

    public void setComponetId(int id){
      if(ids.isEmpty())
          ids = ids+""+id;
      else
          ids=ids+","+id;
        Log.d("ids", ids);
    }
}
