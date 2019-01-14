package com.gnanaoly.flickr.model;

import java.io.Serializable;
import java.text.MessageFormat;


public class FlickrPhotoDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    String id;

    String owner;

    String secret;

    String server;

    int farm;

    String title;

    boolean isPublic;

    boolean isFriend;

    boolean isFamily;

    public FlickrPhotoDetail(String id, String owner, String secret, String server, int farm, String title,
                             boolean isPublic, boolean isFriend, boolean isFamily) {
        this.id = id;
        this.owner = owner;
        this.secret = secret;
        this.server = server;
        this.farm = farm;
        this.title = title;
        this.isPublic = isPublic;
        this.isFriend = isFriend;
        this.isFamily = isFamily;
    }

    public FlickrPhotoDetail() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getFarm() {
        return farm;
    }

    public void setFarm(int farm) {
        this.farm = farm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    public boolean isFamily() {
        return isFamily;
    }

    public void setFamily(boolean family) {
        isFamily = family;
    }


    @Override
    public String toString() {
        return "{" +
                "  id='" + id + '\n' +
                ", owner='" + owner + '\n' +
                ", secret='" + secret + '\n' +
                ", server='" + server + '\n' +
                ", farm='" + farm + '\n' +
                ", title='" + title + '\n' +
                ", ispublic='" + isPublic + '\n' +
                ", isfriend='" + isFriend + '\n' +
                ", isfamily='" + isFamily + '\n' +
                '}';
    }

    public String toLink() {

//       http://farm{farm}.static.flickr.com/{server}/{id}_{secret}.jpg


        return MessageFormat.format("http://farm{0}.static.flickr.com/{1}/{2}_{3}.jpg", farm,server,id, secret);
    }

}
