package com.example.helloandroid;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    private SQLiteDatabase cardDatabase = null;
    private static final String USERTABLENAME = "card";

    public void getDatabase() {
        SqliteHelper db = new SqliteHelper(this, USERTABLENAME, null, 1);
        cardDatabase = db.getWritableDatabase();
    }

    public void clearDatabase() {
        cardDatabase.execSQL("delete from card");
    }

    public void insert(String cardid) {
        clearDatabase();
        ContentValues value = new ContentValues();
        value.put("card_id", cardid);
        cardDatabase.insert(USERTABLENAME, null, value);
    }

    public int update(String cardid, String newId) {
        ContentValues value = new ContentValues();
        value.put("card_id", newId);
        return cardDatabase.update(USERTABLENAME, value, "card_id=?", new String[]{cardid});
    }

    public int delete(String cardid) {
        return cardDatabase.delete(USERTABLENAME, "card_id=?", new String[]{cardid});
    }

    public Cursor simpleQuery() {
        return cardDatabase.rawQuery("select * from card", null);
    }

    public String getCardId() {
        Cursor cc = simpleQuery();

        if (cc.getCount() > 0) {
            cc.moveToFirst();
            return cc.getString(0);
        } else
            return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        //get database for use.
        getDatabase();


        editText = (EditText) this.findViewById(R.id.cardNumber);
        editText.setText(getCardId());

        cardNumber = editText.getText().toString();

        if (cardNumber.equals("")) {

            cardNumber = "7528";
            editText.setText(cardNumber);
        }

        //my owner number

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

                //insert database
                if (!cardNumber.equals("")) {
                    insert(cardNumber);
                }

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
