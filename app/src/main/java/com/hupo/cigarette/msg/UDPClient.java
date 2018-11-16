package com.hupo.cigarette.msg;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.geek.thread.GeekThreadManager;
import com.geek.thread.ThreadPriority;
import com.geek.thread.ThreadType;
import com.geek.thread.task.GeekRunnable;
import com.geek.thread.task.GeekThread;
import com.hupo.cigarette.app.App;
import com.hupo.cigarette.bean.User;
import com.hupo.cigarette.bus.event.SendUDPMsgEvent;
import com.hupo.cigarette.net.Constants;
import com.hupo.cigarette.utils.HttpParamsUtil;
import com.hupo.cigarette.utils.LogUtils;
import com.hupo.cigarette.utils.Utils;
import com.huposoft.commons.utils.UUID;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import static com.hupo.cigarette.msg.ErrorCode.ERROR_SEND;
import static com.hupo.cigarette.net.Constants.UDP_UID;

public class UDPClient {

    private UDPMsgListener listener;
    private DatagramSocket mSocket;
    //消息队列
    private volatile Map<String, String> messageQueue = new HashMap<>();
    private volatile Map<String, UDPMsgEntity> verifyMap = new HashMap<>();
    private volatile Map<String, UDPSendListener> sendListener = new HashMap<>();
    private User user;
    private boolean goFinish = false, isSending = false;
    private static WifiManager.MulticastLock lock;

    private UDPClient() {
    }

    private UDPClient(UDPMsgListener listener) {
        this.listener = listener;
        this.lock = ((WifiManager) App.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE)).createMulticastLock("UDPwifi");
        user = App.getInstance().getUser();
        connectUDP();
    }

    public void finish() {
        lock.release();
        goFinish = true;
        mSocket.close();

    }

    public void sendMsg(SendUDPMsgEvent msg) {
        UDPMsgEntity entity = getMsg(user.getLoginUser().getUuid(), UDP_UID, msg.getMsg());
        messageQueue.put(entity.getMsgUid(), entity.toString());
        sendListener.put(entity.getMsgUid(), msg.getListener());
        sendThread();

    }

    private void connectUDP() {
        GeekThreadManager.getInstance().execute(new GeekRunnable(ThreadPriority.NORMAL) {
            @Override
            public void run() {
                if (mSocket == null || mSocket.isClosed()) {
                    try {
                        lock.acquire();
                        mSocket = new DatagramSocket(9999);
                        //心跳
                        breakHeartThread();
                        //开启接收线程
                        receiveThread();
                    } catch (SocketException e) {
                        LogUtils.e(e.toString());
                    }
                }
            }
        }, ThreadType.REAL_TIME_THREAD);

    }

    private void sendThread() {
        if (isSending) {
            return;
        }
        isSending = true;
        for (String s : messageQueue.keySet()) {
            try {
                if (goFinish || mSocket == null || mSocket.isClosed() || messageQueue.size() < 1) {
                    isSending = false;
                    return;
                }
                Thread.sleep(1000);
                byte[] datas = messageQueue.get(s).getBytes();
                InetAddress address = InetAddress.getByName(Constants.IP);
                final DatagramPacket packet = new DatagramPacket(datas, datas.length, address, Constants.UDP_PORT);
                mSocket.send(packet);
            } catch (UnknownHostException e) {
                sendListener.remove(s);
                sendListener.get(s).faild();
            } catch (IOException e) {
                sendListener.remove(s);
                sendListener.get(s).faild();
            } catch (InterruptedException e) {
                sendListener.remove(s);
                sendListener.get(s).faild();
            }
        }
        messageQueue.clear();
        isSending = false;
    }

    private void breakHeartThread() {
        GeekThreadManager.getInstance().execute(new GeekThread(ThreadPriority.NORMAL) {
            @Override
            public void run() {
                super.run();
                try {
                    while (!goFinish && mSocket != null && !mSocket.isClosed()) {
                        UDPMsgEntity msg = getMsg(user.getLoginUser().getUuid(), UDP_UID, "4,,");
                        verifyMap.put(msg.getMsgUid(), msg);
                        byte[] datas = HttpParamsUtil.encryptString(msg.toString()).getBytes();
                        InetAddress address = InetAddress.getByName(Constants.IP);
                        DatagramPacket packet = new DatagramPacket(datas, datas.length, address, Constants.UDP_PORT);
                        mSocket.send(packet);
                        Thread.sleep(15 * 1000);
                    }

                } catch (UnknownHostException e) {
                    LogUtils.e(e.toString());
                } catch (IOException e) {
                    LogUtils.e(e.toString());
                } catch (InterruptedException e) {
                    LogUtils.e(e.toString());
                }
            }
        }, ThreadType.NORMAL_THREAD);

    }

    private void receiveThread() {
        GeekThreadManager.getInstance().execute(new GeekThread(ThreadPriority.NORMAL) {
            @Override
            public void run() {
                super.run();
                if (mSocket == null || mSocket.isClosed())
                    return;
                try {
                    byte datas[] = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(datas, datas.length, InetAddress.getByName(Constants.IP), Constants.UDP_PORT);
                    while (!goFinish) {
                        // 准备接收数据
                        mSocket.receive(packet);
                        if (packet.getAddress().getHostAddress().equals(Constants.IP)) {
                            String s = new String(packet.getData()).trim();
                            if (!TextUtils.isEmpty(s)) {
                                UDPMsgEntity e = Utils.udpMsgFormat(HttpParamsUtil.decryptString(new String(packet.getData()).trim()));
                                if (e != null) {

                                    if (verifyMap.containsKey(e.getMsgUid()) && verifyMap.get(e.getMsgUid()).getMsg().equals("4,,")) {
                                        verifyMap.remove(e.getMsgUid());
                                    } else {
                                        if(sendListener.containsKey(e.getMsgUid())){
                                            sendListener.get(e.getMsgUid()).success(e);
                                        }else{
                                            listener.receiveMsg(e);
                                        }
                                    }
                                }

                            }

                        }
                    }

                } catch (IOException e) {
                    listener.onError(ERROR_SEND, "接收失败");
                }
            }
        }, ThreadType.REAL_TIME_THREAD);

    }


    public static final class Builder {
        //        private String url;
//        private int port;
        private UDPMsgListener listener;

        public Builder() {
        }

        public Builder listener(UDPMsgListener listener) {
            this.listener = listener;
            return this;
        }

//        public Builder url(String url) {
//            this.url = url;
//            return this;
//        }
//
//        public Builder port(int port) {
//            this.port = port;
//            return this;
//        }


        public UDPClient build() {
            if (listener == null) {
                throw new IllegalArgumentException("缺少参数");
            }
            return new UDPClient(listener);
        }
    }

    public UDPMsgEntity getMsg(String sendUid, String receiverUid, String msg) {
        return new UDPMsgEntity(user.getCurLoginToken(), sendUid, UUID.getUUID16(), Constants.MSG_TYPE_SEND, receiverUid, msg);
    }


}
