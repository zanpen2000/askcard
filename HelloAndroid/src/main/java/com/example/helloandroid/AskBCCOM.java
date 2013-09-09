package com.example.helloandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.util.Log;

public class AskBCCOM extends Activity {

    private Button sendButton;
    private String cardNumber;
    private EditText editText;
    private RadioGroup radioGroup;
    private String bankNumber;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        editText = (EditText) this.findViewById(R.id.cardNumber);
        cardNumber = editText.getText().toString();
        //my owner number
        if (cardNumber.equals("")) {
            cardNumber = "7528";
        }

        bankNumber = this.getString(R.string.bankNumber);

        sendButton = (Button) this.findViewById(R.id.sendButton);
        radioGroup = (RadioGroup) this.findViewById(R.id.radioGroup);

        alertDialog = new AlertDialog.Builder(this).create();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmsManager sms = SmsManager.getDefault();

                int idChecked = radioGroup.getCheckedRadioButtonId();

                String smsText = null;
                switch (idChecked) {
                    case R.id.leftMoney:
                        smsText = "CC欠款#" + cardNumber;
                        break;
                    case R.id.amount:
                        smsText = "CC额度#" + cardNumber;
                        break;
                    case R.id.todayBill:
                        smsText = "CC当日交易#" + cardNumber;
                        break;
                    case R.id.leftBills:
                        smsText = "CC未出账单#" + cardNumber;
                        break;
                    case R.id.currentBill:
                        smsText = "CC本期账单#" + cardNumber;
                        break;
                    case R.id.score:
                        smsText = "CC积分#" + cardNumber;
                        break;
                }
                Log.d("信用卡查询", smsText);
                sms.sendTextMessage(bankNumber, null, smsText, null, null);
                alertDialog.setCancelable(false);
                alertDialog.setMessage("发送成功，请注意查收短信");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alertDialog.show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hello, menu);
        return true;
    }

}
