package com.example.mystartapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.Math;
import androidx.annotation.Nullable;

public class MainActivity extends Activity
{
    private RadioGroup angulos;
    private RadioGroup funciones;
    private ImageView imagenA;
    private TextView resultado;
    private Double mag_ang,resul;
    private Boolean bndf=false,bndt=false;
    String func;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imagenA=findViewById(R.id.image_view);
        funciones=findViewById(R.id.rg);
        angulos=findViewById(R.id.mag);
        resultado=findViewById(R.id.res_text_view);
        resul = Double.valueOf(0);
        mag_ang=Double.valueOf(0);
        funciones.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.sin_radio_button:
                        System.out.println("seno");
                        func="sen";
                        break;
                    case R.id.cos_radio_button:
                        System.out.println("cos");
                        func="cos";
                        break;
                    case R.id.TAN_radio_button:
                        System.out.println("tangente");
                        func="tan";
                        break;

                }
                bndf=true;
            }
        });
        angulos.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(!bndf){
                    Toast.makeText(getBaseContext(),"Seleccione una función",Toast.LENGTH_LONG).show();
                    return;
                }
                switch (checkedId){
                    case R.id.cua_radio_button:
                        System.out.println("45");
                        imagenA.setImageResource(R.drawable.angulo45);
                        mag_ang=convARadian(45.0);
                        resul=calcularFuncion(mag_ang,func);
                        break;
                    case R.id.nov_radio_button:
                        System.out.println("90");
                        imagenA.setImageResource(R.drawable.angulo90);
                        mag_ang=convARadian(90.0);
                        resul=calcularFuncion(mag_ang,func);
                        break;
                    case R.id.cie_radio_button:
                        System.out.println("180");
                        imagenA.setImageResource(R.drawable.angulo180);
                        mag_ang=convARadian(180.0);
                        resul=calcularFuncion(mag_ang,func);
                        break;

                }
                if(bndt){
                    return;
                }
                resultado.setText(resul.toString());
            }
        });

    }
    private Double convARadian(Double ang){
        System.out.println(ang*(Math.PI/180));
        return ang*(Math.PI/180);
    }
    private Double convAG(Double angr){
        System.out.println(angr*(180/Math.PI));
        return angr*(180/Math.PI);
    }

     private Double calcularFuncion(Double angulo, String funcionn){
        Double cal = 0.0;
        bndt=false;
        if(funcionn.equals("sen")){
            //System.out.println("sen");
            cal=Math.sin(angulo);
            System.out.println(cal);
            if(convAG(angulo)==180)
                return Math.floor(cal);
        }
         if(funcionn.equals("cos")){
             //System.out.println("cos");
             cal=Math.cos(angulo);
             System.out.println(cal);
             if(convAG(angulo)==90)
                 return Math.floor(cal);
         }
         if(funcionn.equals("tan")){
             //System.out.println("tan");
             cal=Math.tan(angulo);
             System.out.println(cal);
             if(convAG(angulo)==45)
                 return Math.ceil(cal);
             if(convAG(angulo)==90)
             {
                 resultado.setText("Math Error");
                 bndt=true;
                 return 0.0;
             }
             if(convAG(angulo)==180)
                 return Math.abs(Math.ceil(cal));
         }
         System.out.println(cal);
         return cal;
    }
}
