package com.andyhuang.bluff.FriendPage;

import com.andyhuang.bluff.Object.FriendInformation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FriendPresenter implements FriendContract.Presenter {
    private FriendContract.View friendPageView;
    private DatabaseReference dataBaseRef = FirebaseDatabase.getInstance().getReference();
    private ArrayList<FriendInformation> friendlist = new ArrayList<FriendInformation>();

    public FriendPresenter(FriendContract.View viewInput) {
        friendPageView = viewInput;
        friendPageView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void inviteFriend() {

    }

    @Override
    public void inviteGame() {

    }

    @Override
    public void startGame() {

    }

    @Override
    public void acceptInvite() {

    }

    @Override
    public void readFriendDataFromFireBase() {

    }

    @Override
    public void updateFriendDataToFireBase() {

    }

    @Override
    public void inviteFriend(String email) {


    }
}
