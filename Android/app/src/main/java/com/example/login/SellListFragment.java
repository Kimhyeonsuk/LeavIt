package com.example.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.login.Data.ListItem;
import com.example.login.adapter.TradeListAdapter;
import com.example.login.network.NetworkTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SellListFragment extends Fragment {
    private String TAG = "SellListFragment";
    Fragment fragment_board;

    private Button btn_mypage;
    Fragment fragment_mypage;
    static final ArrayList<ListItem> itemlist = new ArrayList<ListItem>();
    static final ArrayList<ListItem> templist = new ArrayList<ListItem>();
    private TradeListAdapter sell_adapter;
    private boolean isLoading = false;
    private RecyclerView rv_sell_list;

    private SharedPreferences.Editor sharedPreferences_fragment_move_editor;
    private Fragment fragment_posting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell_list, container, false);

        btn_mypage = (Button)view.findViewById(R.id.btn_mypage);
        fragment_board = new BoardFragment();
        fragment_mypage = new MyPageFragment();
        rv_sell_list=(RecyclerView) view.findViewById(R.id.rv_sell_list);
        rv_sell_list.setLayoutManager(new LinearLayoutManager(getActivity()));

        sharedPreferences_fragment_move_editor = getActivity().getSharedPreferences("setting", Context.MODE_PRIVATE).edit();
        fragment_posting = new PostingFragment();

        // ????????? ????????? ?????? ?????????
        this.GetSellPost();
        this.initAdapter();
        this.initScrollListener();

        this.SetItemToBoard();
        this.ToMyPage();

        return view;
    }

    public void GetSellPost()
    {
        Log.w(TAG,"GetSellPost() ?????? ??????");

        NetworkTask networkTask = new NetworkTask(getActivity().getApplicationContext(),"http://3.35.48.170:3000/trade/list?type=false&tradeTime=2020-01-01","GET"); // true??? ???????????????, false??? ???????????????
        try{
            //{"msg":"success","tradeVo":{"tradeId":1,"buyerId":"bmh1211@gmail.com","sellerId":"jae961217@naver.com","boardId":"1","tradeTime":"2020-12-23T00:00:00.000+00:00","boardTitle":null}}
            JSONObject resultObject = new JSONObject(networkTask.execute().get());

            if(resultObject == null)
            {
                Log.e("????????????","????????????");
                return;
            }

            String resultString = resultObject.getString("msg");

            if(resultString.equals("failed")){
                Toast.makeText(getActivity(),resultString, Toast.LENGTH_SHORT).show();
            }
            else if(resultString.equals("success")){
                Toast.makeText(getActivity(),resultString, Toast.LENGTH_SHORT).show();

                JSONObject sellObject = resultObject.getJSONObject("tradeVo");
                Log.w("SellListFragment",sellObject.toString());

                String title, tradeTime, userID;
                int boardId;
                title = sellObject.getString("title");
                tradeTime = sellObject.getString("tradeTime").substring(0, 10);
                userID = sellObject.getString("buyerId");
                boardId = sellObject.getInt("boardId");

                ListItem temp = new ListItem();
                temp.setTitle(title);
                temp.setTradeTime(tradeTime);
                temp.setUserID(userID);
                temp.setBoardId(boardId);

                for(int i=0;i<30;i++){
                    // ?????? ????????? ????????? ????????? itemlist??? ??????
                    // todo : 50??? ????????? tradeVo??? ???????????? ??????
                    itemlist.add(temp);
                }

                // ?????? ????????? ?????????
                if(itemlist.size()<10){
                    for(int i=0;i<itemlist.size();i++){
                        templist.add(itemlist.get(i));
                    }
                }
                else{
                    for(int i=0;i<10;i++){
                        templist.add(itemlist.get(i));
                    }
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        } catch (
                ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void initAdapter(){
        sell_adapter = new TradeListAdapter(templist);
        rv_sell_list.setAdapter(sell_adapter);
    }

    public void initScrollListener(){
        rv_sell_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d(TAG, "onScrollStateChanged : " + newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d(TAG, "onScrolled : dx " + dx + ", dy " + dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == templist.size() - 1) {
                        // ???????????? ???????????? ??????
                        dataMore();
                        isLoading = true;
                        Toast.makeText(getActivity(), "????????? ??? ??? ??????", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void dataMore(){
        Log.w(TAG, "dataMore : ");

        // ?????? ???????????? ????????????(?????? ??????) ?????? ??????
        templist.add(null);
        sell_adapter.notifyItemInserted(templist.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // ?????????????????? ?????? ?????????(null)??? ?????? ????????????
                templist.remove(templist.size() - 1);
                // ?????? ?????? => ????????? ?????????????????? list.size()??? ????????? list.size()-1 ??? ?????? ?????? ??????
                sell_adapter.notifyItemRemoved(templist.size());

                int currentSize = templist.size();
                int nextLimit = currentSize + 10;

                // ???????????? ?????? ?????? ??? ???????????? 10?????? ????????? ???????????? ?????????
                for (int i = currentSize; i < nextLimit; i++) {
                    if (i == itemlist.size())
                        return;

                    templist.add(itemlist.get(i));
                }

                sell_adapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);
    }

    public void SetItemToBoard(){
        sell_adapter.setOnItemClickListener(new TradeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                sharedPreferences_fragment_move_editor.putString("fragment_move","SellListFragment").commit();

                // ????????? ??????, ?????? ??????, ????????? ?????? ???????????? ??????
                ListItem temp = sell_adapter.listviewitem.get(position);
                String title = temp.getTitle();
                String tradeTime = temp.getTradeTime();
                String userID = temp.getUserID();
                int boardId = temp.getBoardId();

                Log.w(TAG,"SetItemToBoard( title : "+title+", tradeTime : "+tradeTime+", userID : "+userID+", boardId : "+boardId+" )");

                // bundle??? ?????? ????????? ???????????? boardId??? ??????
                Bundle bundle = new Bundle();
                bundle.putInt("boardId",boardId);
                fragment_posting.setArguments(bundle);

                // ?????????????????? ?????? -> ????????? ?????????????????? fragment1 ?????? ???????????????
                ((MainPageActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment_posting).commit();
            }
        });
    }

    public void ToMyPage(){
        btn_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainPageActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment_mypage).commit();
            }
        });
    }
}