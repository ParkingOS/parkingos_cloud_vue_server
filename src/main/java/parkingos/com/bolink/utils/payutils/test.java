package parkingos.com.bolink.utils.payutils;

public class test {
    public static void main (String args[]) throws Exception {
        String str="weixin://wxpay/bizpayurl?pr=pIxXXXX";
        String pic = GooglePicture.QRfromGoogle(str);
        System.out.println(pic);
    }
}
