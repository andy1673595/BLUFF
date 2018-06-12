# BLUFF
BLUFF是一款能讓使用者連線視訊吹牛的 App，<br />
讓分隔兩地的好朋友可以邊玩吹牛遊戲，邊聊天喝酒<br />
[<img src="https://github.com/andy1673595/BLUFF/blob/master/pictures/google-play-badge.png" width="180">](https://play.google.com/store/apps/details?id=com.andyhuang.bluff)


# Featrue
* 登入以及創建帳號
  * 使用者可以使用 facebook 登入
  * 使用者能以自定義 Email 和 password 創建帳號並登入

* Drawer 選單
  * 可以右滑拉出 drawer 選單點選前往各個頁面
  * 可以點選右上方選單按鈕叫出選單
  
* 邀請大廳
  * 輸入其他使用者 Email 發送好友邀請
  * 收到好友邀請後可以按打勾確認邀請，或者叉叉刪除並且拒絕邀請
  * 可點選好友欄位觀看好友個人資料
  * 勾選想要邀請遊戲的對象右邊的框框，按下下方開啟遊戲房間進行邀請
  * 若有人拒絕邀請則遊戲房間會自動關閉，請重新進行邀請
 
* 隨機對戰 (兩人吹牛)
  * 點選選單 "隨機對戰" 即可加入佇列等待遊戲
  
* 遊戲房間頁面
  * 可以點開上方 "目前玩家" 觀看進入房間的人數
  * 房主點選 Start 便可開始準備遊戲，待所有玩家按下 Ready 之後便會自動開啟新的一局
  * 輪到該玩家的回合時，可以按 "喊" 或 "骰子圖案" 叫出往上喊的視窗讓使用者選取骰子種類與數目
  * 其他玩家可以按 "抓" 或 "手掌圖案" 抓上一個玩家所喊的骰子
  * 遊戲結算後可以選擇開啟新的一局或者退出遊戲，若有人退出遊戲則所在的遊戲房間關閉
  
* 視訊功能
  * 若玩家剛好為兩個人，則可以點選右上方視訊按鈕開啟或關閉視訊功能
  * 請注意若兩個使用者太過接近，會有爆音的情況
  * 舊版本手機在關閉視訊時會有閃退的清況
  
* 個人頁面
  * 可讓使用者從自身手機裡選取圖片，自定義大頭照
  * 可讓使用者改變個性化簽名

* 排行榜
  * 可觀看 firebase 上面總次數、兩人遊戲贏的總次數以及勝率等排行前三名
  * 勝率目前暫定 5 場以上兩人遊戲才有進入排行


# Library And API
* Firebase
* WebRTC
* PermissionsDispatcher
* Facebook


# Screenshot
<img src="https://github.com/andy1673595/BLUFF/blob/master/pictures/Screenshot_2018-05-28-15-28-39-509_com.andyhuang.bluff.png" width="240"><img src="https://github.com/andy1673595/BLUFF/blob/master/pictures/Screenshot_2018-06-13-01-08-23-223_com.andyhuang.bluff.png" width="240"> 
<img src="https://github.com/andy1673595/BLUFF/blob/master/pictures/Screenshot_2018-05-28-15-32-25-202_com.andyhuang.bluff.png" width="240"><img src="https://github.com/andy1673595/BLUFF/blob/master/pictures/Screenshot_2018-05-28-15-35-46-061_com.andyhuang.bluff.png" width="240"> 
<img src="https://github.com/andy1673595/BLUFF/blob/master/pictures/Screenshot_2018-06-05-15-18-32-925_com.andyhuang.bluff.png" width="240">


# Requirement
* android 4.4以上
* 如果想要更好的體驗建議 android 7 以上


# Version
* 1.1.1 - 2018/06/05

  * 修正邀請時容易閃退的 Bug
  
* 1.0 - 2018/05/28

  * First release
 

# Contacts
Andy Huang <br />
andy5953761@gmail.com 
