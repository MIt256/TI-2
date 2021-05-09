package com.TiLab;

import java.net.URL;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class PrimaryController {

    @FXML
    private TextField text;

    @FXML
    private TextField resultHash;

    @FXML
    private Button encode;

    @FXML
    private TextField firstNum;

    @FXML
    private TextField secondNum;

    @FXML
    private TextField resultPodp;

    @FXML
    private TextField exp;

    @FXML
    private CheckBox checkBox;

    @FXML
    private TextField podp;

    @FXML
    private TextField messageHash;

    @FXML
    private TextField podpHash;

    @FXML
    private Button check;


    //
    public long firstNumber,secondNumber,podpNum;
    public String inputText;
    static int Letters=63;
    public long r,e;

    @FXML
    void initialize() {

    }
    //проверка галочки
    @FXML
    void expCheck(MouseEvent event) {
        if (checkBox.isSelected()){
            exp.setDisable(true);
        } else {
            exp.setDisable(false);
        }
    }

    // Проверка простые ли числа
    private static boolean isSimple(long a) {
        for (long i = 2; i <= Math.sqrt(a); i++) {
            if (a % i == 0) {
                return false;
            }
        }
        return true;
    }
    // Являются ли числа взаимно простыми
    public static boolean isTwoSimple(long a, long b) {
        if (a == b) {
            return a == 1;
        } else {
            if (a > b) {
                return isTwoSimple(a - b, b);
            } else {
                return isTwoSimple(b - a, a);
            }
        }
    }
    // Вычисляем значение открытой экспненты e для генерации
    private static long getE(long f) {
        ArrayList<Long> valArr = new ArrayList<Long>();
        long e = f - 1;
        for (int i = 2; i < f; i++) {
            if (isSimple(e) && isTwoSimple(e, f)) {
                valArr.add(e);
            }
            e--;
        }
        Random random = new Random();
        int index = random.nextInt(valArr.size());
        return valArr.get(index);
    }
    // Расширенный алгоритм Евклида
    public static int getExtendGcd( int a, int b) {

        int d0 = a; int d1 = b;
        int x0 = 1; int x1 = 0;
        int y0 = 0; int y1 = 1;
        while(d1 > 1) {
            int q = d0 / d1;
            int d2 = d0 % d1;
            int x2 = x0 - q * x1;
            int y2 = y0 - q * y1;
            d0 = d1; d1 = d2;
            x0 = x1; x1 = x2;
            y0 = y1; y1 = y2;
        }

        return (y1);
    }

    @FXML
    void encodeFunc(MouseEvent event) {// главный метод
        firstNumber = Long.parseLong(firstNum.getText());
        secondNumber = Long.parseLong(secondNum.getText());

        inputText = text.getText();
        inputText = inputText.toUpperCase(Locale.ENGLISH);

        r = firstNumber * secondNumber;
        long f = (firstNumber-1)*(secondNumber-1);

        if (checkBox.isSelected()) {
            e = getE(f);
            exp.setText(String.valueOf(e));
        } else {
            e = Long.parseLong(exp.getText());
        }

        // Расширенный алгоритм Евклида
        long d = Long.valueOf(getExtendGcd((int) f,(int) e ));
        if (d < 0) {
            d += f;
        }

        Long hash = getHash(inputText, r);

        Long res = Power(hash, d, r);

        resultHash.setText(hash.toString());
        resultPodp.setText(res.toString());

    }

    @FXML
    void checkFunc(ActionEvent event) {
        inputText = text.getText();
        inputText = inputText.toUpperCase(Locale.ENGLISH);

        podpNum = Long.parseLong(podp.getText());

        Long newHash = getHash(inputText, r);
        Long newRes = Power(podpNum, e, r);

        System.out.println("power");
        System.out.println(newRes);
        System.out.println("хэш");
        System.out.println(newHash);

        if (newHash == newRes){
            System.out.println("все правильно");
        }else {  System.out.println("все плохо");}

        messageHash.setText(newHash.toString());
        podpHash.setText(newRes.toString());

    }




    //Алгоритм возведения в степень
    public static long Power(long x, long y, long N) {
        if (y == 0) return 1;
        long z = Power(x, y / 2, N);
        if (y % 2 == 0)
            return (z * z) % N;
        else
            return (x * z * z) % N;
    }

    private static long getHash(String s, long n){
        long h = 100;
        for (int i = 0; i < s.length(); i++){
            h = (h + (int)s.charAt(i))*(h + (int)s.charAt(i)) % n;
        }
        return h;
    }
}
