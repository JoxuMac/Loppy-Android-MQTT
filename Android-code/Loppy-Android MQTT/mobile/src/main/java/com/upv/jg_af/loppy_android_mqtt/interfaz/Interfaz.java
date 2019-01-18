/*
* Proyecto      : Lopy-Android MQTT
* Autor         : Josué Gutiérrez Durán
* Autor         : Alex Feng
* Creación      : 16/01/2019
* Clase         : Interfaz
* Descripción   : Clase referencial a la Actividad Interfaz
*/

// PAQUETE
package com.upv.jg_af.loppy_android_mqtt.interfaz;

// BIBLIOTECAS
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.upv.jg_af.loppy_android_mqtt.persistencia.MqttBroker;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Interfaz extends AppCompatActivity {

    // MQTTBroker
    MqttBroker mqttbroker;

    // VARIABLES
    private boolean get_temp = false;
    private boolean get_hum = false;
    private boolean get_lig = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mobile);

        // DESACTIVAR BARRA SUPERIOR
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        // ELEMENTOS VISUALES
        Button btn_temp = (Button) findViewById(R.id.btnTemp);
        Button btn_hum = (Button) findViewById(R.id.btnHum);
        Button btn_lig = (Button) findViewById(R.id.btnLig);
        Button btn_red = (Button) findViewById(R.id.btnRed);
        Button btn_blue = (Button) findViewById(R.id.btnBlue);
        Button btn_green = (Button) findViewById(R.id.btnGreen);
        Button btn_yellow = (Button) findViewById(R.id.btnYellow);
        Button btn_off = (Button) findViewById(R.id.btnOff);

        // BOTON TEMPERATURA
        btn_temp.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                get_temp = true;
                try {
                    mqttbroker.sendMessage("get","temp");
                } catch (MqttException e) {
                    Log.w("Debug", e);
                }
            }
        });

        // BOTON HUMEDAD
        btn_hum.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                get_hum = true;
                try {
                    mqttbroker.sendMessage("get","hum");
                } catch (MqttException e) {
                    Log.w("Debug", e);
                }
            }
        });

        // BOTON LUMINOSIDAD
        btn_lig.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                get_lig = true;
                try {
                    mqttbroker.sendMessage("get","lig");
                } catch (MqttException e) {
                    Log.w("Debug", e);
                }
            }
        });

        // BOTON RED
        btn_red.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                try {
                    mqttbroker.sendMessage("set","red");

                    Toast.makeText((Activity) v.getContext(), "Enviado!",
                            Toast.LENGTH_SHORT).show();
                } catch (MqttException e) {
                    Log.w("Debug", e);
                }
            }
        });

        // BOTON BLUE
        btn_blue.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                try {
                    mqttbroker.sendMessage("set","blue");

                    Toast.makeText((Activity) v.getContext(), "Enviado!",
                            Toast.LENGTH_SHORT).show();
                } catch (MqttException e) {
                    Log.w("Debug", e);
                }
            }
        });

        // BOTON GREEN
        btn_green.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                try {
                    mqttbroker.sendMessage("set","green");

                    Toast.makeText((Activity) v.getContext(), "Enviado!",
                            Toast.LENGTH_SHORT).show();
                } catch (MqttException e) {
                    Log.w("Debug", e);
                }
            }
        });

        // BOTON YELLOW
        btn_yellow.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                try {
                    mqttbroker.sendMessage("set","yellow");

                    Toast.makeText((Activity) v.getContext(), "Enviado!",
                            Toast.LENGTH_SHORT).show();
                } catch (MqttException e) {
                    Log.w("Debug", e);
                }
            }
        });

        // BOTON OFF
        btn_off.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                try {
                    mqttbroker.sendMessage("set","off");

                    Toast.makeText((Activity) v.getContext(), "Enviado!",
                            Toast.LENGTH_SHORT).show();
                } catch (MqttException e) {
                    Log.w("Debug", e);
                }
            }
        });

        // LANZAR SERVIDOR MQTT
        startMqtt();
    }

    // SERVIDOR MQTT
    private void startMqtt(){
        mqttbroker = new MqttBroker(getApplicationContext());
        mqttbroker.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) {
                if(get_temp && topic.equals("temp")) {
                    Toast toast1 = Toast.makeText(getApplicationContext(), "Temperatura: "+mqttMessage.toString().substring(0,mqttMessage.toString().length()-3)+" ºC", Toast.LENGTH_SHORT);
                    toast1.setGravity(Gravity.CENTER, 0,0 );
                    toast1.show();

                    get_temp = false;
                }

                if(get_hum && topic.equals("hum")){
                    Toast toast1 = Toast.makeText(getApplicationContext(), "Humedad: "+mqttMessage.toString().substring(0,mqttMessage.toString().length()-3)+ " H.R.", Toast.LENGTH_SHORT);
                    toast1.setGravity(Gravity.CENTER, 0,0 );
                    toast1.show();

                    get_hum = false;
                }

                if(get_lig && topic.equals("lig")){
                    Toast toast1 = Toast.makeText(getApplicationContext(), "Luminosidad: "+mqttMessage.toString().substring(0,mqttMessage.toString().length()-3), Toast.LENGTH_SHORT);
                    toast1.setGravity(Gravity.CENTER, 0,0 );
                    toast1.show();

                    get_lig = false;
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }
}
