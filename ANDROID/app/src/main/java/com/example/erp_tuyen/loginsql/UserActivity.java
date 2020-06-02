package com.example.erp_tuyen.loginsql;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserActivity extends Activity {

    EditText edtOldPw, edtNewPw, edtReNewPw;
    TextView tvUser,tvTitleUs;
    Button btnSubmit;
    ProgressBar progressBar;

    String un,pw,hoten;
    Boolean isUpdated;

    String z;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        edtOldPw=(EditText)this.findViewById(R.id.edtOldPw);
        edtNewPw=(EditText)this.findViewById(R.id.edtNewPw);
        edtReNewPw=(EditText)this.findViewById(R.id.edtReNewPw);
        btnSubmit=(Button)this.findViewById(R.id.btnSubmit);
        tvUser=(TextView)this.findViewById(R.id.tvUser);
        tvTitleUs=(TextView)this.findViewById(R.id.tvTitleUs);
        progressBar=(ProgressBar)this.findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);

        Bundle extras=getIntent().getExtras(); //dùng cái này để hứng gói tin hồi nãy.
        un=extras.getString("USERNAME","N/A");//lấy giá trị của cái key là USERNAME
        pw=extras.getString("PASSWORD","N/A");// như trên
        hoten=extras.getString("HOTEN","N/A");// vẫn rứa

        tvUser.setText(hoten);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DoUpdate up= new DoUpdate();
                up.execute("");//dùng gọi... ak mà nói trong bài trước rồi
            }
        });
    }

    void finishUpdate(){ //tạo gói Intent để gửi trả cho cái thằng kia nãy nó gửi và nó đang đợi trả
        if(isUpdated){
            Intent intent= new Intent(this,MainActivity.class); //bắn lại cho thằng MainActivity.class
            intent.putExtra("PASSWORD",pw);
            this.setResult(RESULT_OK,intent);
            super.finish(); //k có cái này thì nó k gửi trả, nghĩa là bên kia sẽ nghĩ là chưa có gửi tới
        }
    }

    public class DoUpdate extends AsyncTask<String,String,String> {
        @Override
            protected String doInBackground(String... strings) {

            String oPw,nPw,rnPw;
            oPw=edtOldPw.getText().toString();
            nPw=edtNewPw.getText().toString();
            rnPw=edtReNewPw.getText().toString();

            isUpdated=false;
            if(oPw.trim().equals("")||nPw.trim().equals("")||rnPw.trim().equals("")){
                z="Vui lòng nhập đủ thông tin";
            }else if(!oPw.equals(pw)){
                z="Mật khẩu cũ không đúng !";
            }else if(!nPw.equals(rnPw) ){
                z="Mật khẩu không trùng khớp !";
            }
            else if(pw.equals(rnPw) ){
                z="Mật khẩu mới phải khác với mật khẩu cũ !";
            } else {
                try {
                    Connection conn = SERVER.Connect();
                    if (conn != null) {
                        int resultSet;
                        String query = "Update tbl_user set matkhau='" + HASH.md5(rnPw)  + "' where  matkhau='" + HASH.md5(pw) + "' and tendangnhap='"+un+"'";
                        Statement stmt = conn.createStatement();
                        resultSet= stmt.executeUpdate(query);//thực thi lệnh, trả về số dòng thực hiện dc

                        if(resultSet==1)
                        {
                            z="Mật khẩu đã được thay đổi ! ";
                            pw=rnPw;
                            isUpdated=true;//lại đánh dấu chủ quyền
                            conn.close();
                        }
                        else
                        {
                            z="Không thể hoàn tất thay đổi mật khẩu ! ";
                        }
                    } else {
                        z="Cập nhật không thành công ! Lỗi kết nối !";
                    }
                } catch (Exception ex)
                {
                    Log.e("ERUs", ex.getMessage());
                }
            }
            return z;
        }


        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            //super.onProgressUpdate(values);
            //Cập nhật thông tin như thay đổi giá trị progressBar ở đây !
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(UserActivity.this, s, Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
            finishUpdate();//gọi hàm trả lui gói Intent
        }
    }

}
