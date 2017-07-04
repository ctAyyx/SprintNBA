package com.ct.sprintnba_demo01;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ct.sprintnba_demo01.base.BaseActivity;
import com.ct.sprintnba_demo01.mutils.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import butterknife.OnClick;

public class PayForActivity extends BaseActivity {
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    private static final String APPID = "2017062607572060";
    private static final String RSA2 = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCdft2qrcUYUxEgIZyGQFjUZgZEYGNlMljEKcdR/llOj0Ixzdxijoktpt+m7t7Xi6Oa4+9ly6m6oXfGT5dj04qLzOtUUddSan8iXHfXzGT+jb7FcDUxBPQjr8GIbXb3cWU2Hq3hLR2ZWMTveW2UFxh5c4wfQJ4sx+tawqF+wIXejgapH5keHLKSghIblKBD1HYtJDod0ZSzfhnphZExB4ceTso7lnjRIWXtICiBZNyN2QpRTR9w+Yii/MWYsO+KCBRHS+xtvg89O09jOPOPl6SJqDvGtUTqWmaszJZyidLK2LcsmN4XmS3LBI16gHlPGVoiz50Bv70Rwb9QH8aM/CYtAgMBAAECggEAXZdDYKbMxgyu05pFqp3Ya7eJJ97jkgJeL4voL3hnV8DYBEKiIUs0h5VvuIKgmBQaD/Q9kg95+Db1tzoa666M2VDX046i1w4EKMYsTWff6EkRQcDRSyT3c7GA33K1TIvtKkEG8RXfTf4rYWeWT3d+CsY4UWYT2OagnJmmYG1F4B81ROuxBkoqwfHncKXazYcEC+kEXnB88D723n51JMe5Y6Mw4fuQeQ0h7wrYJkGHVfMWa0nAQzClmVrvDweDWaKzaAuRQ8wN92k1DT+8dyZV6+yfF1tdjFdWggc/ZOHHjHzIOENZdZyEQF0/LvRpFaW4Qc723gfaqPxjs1w2RmXJAQKBgQDRszsM5fTCOnuDt2rxEJxL9OsNpqfOgOrJpCJyCUld/PaxFNYDzI71C2V8kEtU9LKekwbzzXTiz3odPaY0Ow4BHwCV9IwcCbmmR87V8lYkApxJ0TFOqThwXdVx4ZTMnTpbQqrean76+F/lWsq5iaUfpC5jWqsDCIGnHKsW1PbCwQKBgQDAROerojWk3h45ck6JzueDLyN1wxzk/S46O+0L7TODk9XX7DMnI5nXuJTSJvNu32WdZxIUViZwh3+8gNXGKq9XhatR9IJt6ptM3dR5h87qIIZXXCs41oojUF38r0rHC7ewK8V7d1hvpaN7qrTjQrRQHGpivHxWlrB2Omk34Cu6bQKBgQCD+CPFbkKmqV9p+VhX9XgnZS84HBWnJAKee6kh0y5U9AncT5yQL4AYPt/TkIuYH5b5rNXHQRm6U5AqLs4WOMQLeJNJy+kRDB3Fw5MrmaMUboAlBH3fXyehr6vQS4L1ck1zeXVHHAZls0HGeWjzcuh0DUMUS88rzYKqxKP3bncNAQKBgQCRumhBzbONH5GXBw2CJCGTa8IbuuQGp+NG3i7gMEwwOXgGrY9j5fbv64ZY9o7iBU7Wi83HPjjqZ7konhnplmbKFpVwi7vkJcLJ8wTdjFMHX9mTE1frP6j59SsXn6E01qvGSpbm6uFL1ibgT9XEvsU+RxtXdPdlBdf/9GvVnwJlzQKBgQCaalKhrSwkoVlIkBuoXDLFK37DCTRYTsfbqkpH6afFpv9siTuU2bqdLt+5hYQvwfP6NXJAKkPDd68yYqTnqiwUMddvQ5+VXrNsfTrQB0NZR63kI92YTUUuAjzibYEveeSk7cpbqfa2pPw3zPinbkX/e1vjgVaLAwOCXq6ySognew==";

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(PayForActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayForActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(PayForActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(PayForActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_pay_for;
    }

    @Override
    public void initViewsAndEvents(Bundle savedInstanceState) {

    }

    @OnClick({R.id.btn_payFor_01, R.id.btn_payFor_02})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_payFor_01:
                payFor();
                break;
            case R.id.btn_payFor_02:
                shouQuan();
                break;
        }
    }

    private void payFor() {
        //创建支付订单参数列表
        Map<String, String> keyValues = new HashMap();
        keyValues.put("app_id", APPID);
        keyValues.put("biz_content", "{\"timeout_express\":\"30m\",\"product_code\":\"QUICK_MSECURITY_PAY\",\"total_amount\":\"0.01\",\"subject\":\"1\",\"body\":\"我是测试数据\",\"out_trade_no\":\"" + getOutTradeNo() + "\"}");

        keyValues.put("charset", "utf-8");

        keyValues.put("method", "alipay.trade.app.pay");

        keyValues.put("sign_type", "RSA2");

        keyValues.put("timestamp", "2017-06-26 17:50:53");

        keyValues.put("version", "1.0");

        //构建支付订单参数信息
        List<String> keys = new ArrayList(keyValues.keySet());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = keyValues.get(key);
            sb.append(buildKeyValue(key, value, true));
            sb.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = keyValues.get(tailKey);
        sb.append(buildKeyValue(tailKey, tailValue, true));


        final String orderInfo = sb + "&" + getSign(keyValues, RSA2, true);


        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PayForActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("sprint", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }

    private void shouQuan() {

        Map<String, String> keyValues = new HashMap<String, String>();

        // 商户签约拿到的app_id，如：2013081700024223
        keyValues.put("app_id", APPID);

        // 商户签约拿到的pid，如：2088102123816631
        keyValues.put("pid", APPID);

        // 服务接口名称， 固定值
        keyValues.put("apiname", "com.alipay.account.auth");

        // 商户类型标识， 固定值
        keyValues.put("app_name", "mc");

        // 业务类型， 固定值
        keyValues.put("biz_type", "openservice");

        // 产品码， 固定值
        keyValues.put("product_id", "APP_FAST_LOGIN");

        // 授权范围， 固定值
        keyValues.put("scope", "kuaijie");

        // 商户唯一标识，如：kkkkk091125
      //  keyValues.put("target_id", target_id);

        // 授权类型， 固定值
        keyValues.put("auth_type", "AUTHACCOUNT");

        // 签名类型
        //keyValues.put("sign_type", rsa2 ? "RSA2" : "RSA");
    }


    /**
     * 拼接键值对
     *
     * @param key
     * @param value
     * @param isEncode
     * @return
     */
    private String buildKeyValue(String key, String value, boolean isEncode) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }
        return sb.toString();
    }

    /**
     * 对支付参数信息进行签名
     *
     * @param map 待签名授权信息
     * @return
     */

    public String getSign(Map<String, String> map, String rsaKey, boolean rsa2) {
        List<String> keys = new ArrayList<String>(map.keySet());
        // key排序
        Collections.sort(keys);

        StringBuilder authInfo = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            authInfo.append(buildKeyValue(key, value, false));
            authInfo.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        authInfo.append(buildKeyValue(tailKey, tailValue, false));

        String oriSign = SignUtils.sign(authInfo.toString(), rsaKey, rsa2);
        String encodedSign = "";

        try {
            encodedSign = URLEncoder.encode(oriSign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "sign=" + encodedSign;
    }

    /**
     * 要求外部订单号必须唯一。
     *
     * @return
     */
    private static String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }


    static class SignUtils {

        private static final String ALGORITHM = "RSA";

        private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

        private static final String SIGN_SHA256RSA_ALGORITHMS = "SHA256WithRSA";

        private static final String DEFAULT_CHARSET = "UTF-8";

        private static String getAlgorithms(boolean rsa2) {
            return rsa2 ? SIGN_SHA256RSA_ALGORITHMS : SIGN_ALGORITHMS;
        }

        public static String sign(String content, String privateKey, boolean rsa2) {
            try {
                PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                        Base64.decode(privateKey));
                KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);
                PrivateKey priKey = keyf.generatePrivate(priPKCS8);

                java.security.Signature signature = java.security.Signature
                        .getInstance(getAlgorithms(rsa2));

                signature.initSign(priKey);
                signature.update(content.getBytes(DEFAULT_CHARSET));

                byte[] signed = signature.sign();

                return Base64.encode(signed);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

    }


    public class PayResult {
        private String resultStatus;
        private String result;
        private String memo;

        public PayResult(Map<String, String> rawResult) {
            if (rawResult == null) {
                return;
            }

            for (String key : rawResult.keySet()) {
                if (TextUtils.equals(key, "resultStatus")) {
                    resultStatus = rawResult.get(key);
                } else if (TextUtils.equals(key, "result")) {
                    result = rawResult.get(key);
                } else if (TextUtils.equals(key, "memo")) {
                    memo = rawResult.get(key);
                }
            }
        }

        @Override
        public String toString() {
            return "resultStatus={" + resultStatus + "};memo={" + memo
                    + "};result={" + result + "}";
        }

        /**
         * @return the resultStatus
         */
        public String getResultStatus() {
            return resultStatus;
        }

        /**
         * @return the memo
         */
        public String getMemo() {
            return memo;
        }

        /**
         * @return the result
         */
        public String getResult() {
            return result;
        }
    }

    public class AuthResult {

        private String resultStatus;
        private String result;
        private String memo;
        private String resultCode;
        private String authCode;
        private String alipayOpenId;

        public AuthResult(Map<String, String> rawResult, boolean removeBrackets) {
            if (rawResult == null) {
                return;
            }

            for (String key : rawResult.keySet()) {
                if (TextUtils.equals(key, "resultStatus")) {
                    resultStatus = rawResult.get(key);
                } else if (TextUtils.equals(key, "result")) {
                    result = rawResult.get(key);
                } else if (TextUtils.equals(key, "memo")) {
                    memo = rawResult.get(key);
                }
            }

            String[] resultValue = result.split("&");
            for (String value : resultValue) {
                if (value.startsWith("alipay_open_id")) {
                    alipayOpenId = removeBrackets(getValue("alipay_open_id=", value), removeBrackets);
                    continue;
                }
                if (value.startsWith("auth_code")) {
                    authCode = removeBrackets(getValue("auth_code=", value), removeBrackets);
                    continue;
                }
                if (value.startsWith("result_code")) {
                    resultCode = removeBrackets(getValue("result_code=", value), removeBrackets);
                    continue;
                }
            }

        }

        private String removeBrackets(String str, boolean remove) {
            if (remove) {
                if (!TextUtils.isEmpty(str)) {
                    if (str.startsWith("\"")) {
                        str = str.replaceFirst("\"", "");
                    }
                    if (str.endsWith("\"")) {
                        str = str.substring(0, str.length() - 1);
                    }
                }
            }
            return str;
        }

        @Override
        public String toString() {
            return "resultStatus={" + resultStatus + "};memo={" + memo + "};result={" + result + "}";
        }

        private String getValue(String header, String data) {
            return data.substring(header.length(), data.length());
        }

        /**
         * @return the resultStatus
         */
        public String getResultStatus() {
            return resultStatus;
        }

        /**
         * @return the memo
         */
        public String getMemo() {
            return memo;
        }

        /**
         * @return the result
         */
        public String getResult() {
            return result;
        }

        /**
         * @return the resultCode
         */
        public String getResultCode() {
            return resultCode;
        }

        /**
         * @return the authCode
         */
        public String getAuthCode() {
            return authCode;
        }

        /**
         * @return the alipayOpenId
         */
        public String getAlipayOpenId() {
            return alipayOpenId;
        }
    }
}
