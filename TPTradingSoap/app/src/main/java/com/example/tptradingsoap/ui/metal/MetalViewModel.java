package com.example.tptradingsoap.ui.metal;


import android.app.Application;
import android.content.Context;
import android.text.format.DateFormat;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tptradingsoap.ui.metal.metalhelper.MetalDto;
import com.example.tptradingsoap.ui.metal.metalhelper.MetalRecyclerViewAdapter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Time;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MetalViewModel extends ViewModel {
    private final Context _context;
    private final MutableLiveData<MetalRecyclerViewAdapter> metalData;
    public MetalViewModel(Context context) {
        _context = context;
        metalData = new MutableLiveData<>();
        ArrayList<MetalDto> list = getMetalData();
        //list.add(new MetalDto());
        //list.get(0).Aciklama="Açıklama";
        //list.get(0).Kod="test";
        //list.get(0).Alis=new Float("0.234");
        //list.get(0).Satis=new Float("0.12");
        //SimpleDateFormat simpledateformat = new SimpleDateFormat("d.M.yyyy HH:mm:ss");
        //list.get(0).GuncellenmeZamani=simpledateformat.parse(DateFormat.format("d.M.yyyy HH:mm:ss", new Date()).toString(),new ParsePosition(0));
//
//
        //list.add(new MetalDto());
        //list.get(1).Aciklama="Açıklama";
        //list.get(1).Kod="test";
        //list.get(1).Alis=new Float("0.232");
        //list.get(1).Satis=new Float("0.3434");
        //list.get(1).GuncellenmeZamani=simpledateformat.parse(DateFormat.format("d.M.yyyy HH:mm:ss", new Date()).toString(),new ParsePosition(0));


        metalData.setValue(new MetalRecyclerViewAdapter(_context,list));
    }

    private ArrayList<MetalDto> getMetalData() {
        String soapRequest = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                +"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\""
                +"        xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
                +"   <soap:Header>"
                +"        <AuthHeader xmlns=\"http://data.altinkaynak.com/\">"
                +"            <Username>AltinkaynakWebServis</Username>"
                +"            <Password>AltinkaynakWebServis</Password>"
                +"       </AuthHeader>"
                +"    </soap:Header>"
                +"    <soap:Body>"
                +"        <GetGold xmlns=\"http://data.altinkaynak.com/\" />"
                +"    </soap:Body>"
                +"</soap:Envelope>";

        try {
            URL url =  new URL("http://data.altinkaynak.com/DataService.asmx");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","text/xml; charset=utf-8");
            connection.setRequestProperty("Content-Length","length");
            connection.setRequestProperty("SOAPAction","http://data.altinkaynak.com/GetGold");
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            os.write(soapRequest.getBytes());
            os.flush();
            os.close();

            int responseCode = connection.getResponseCode();
            BufferedReader br = null;
            BufferedReader reader;
            if (responseCode==HttpURLConnection.HTTP_OK)  {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder xmlSb = new StringBuilder();
                String strCurrentLine;
                while ((strCurrentLine = br.readLine()) != null) {
                    xmlSb.append(strCurrentLine);
                }
                String  xmlStr = xmlSb.toString();
                xmlStr = xmlStr.replace("&lt;","<");
                xmlStr = xmlStr.replace("&gt;",">");

                xmlStr = xmlStr.substring(xmlStr.indexOf("<GetGoldResult>")+("<GetGoldResult>").length(),xmlStr.indexOf("</GetGoldResult>"));

                Document xmlDoc = DocumentBuilderFactory
                        .newInstance()
                        .newDocumentBuilder()
                        .parse(
                                new InputSource(
                                        new StringReader(xmlStr)
                                )
                        );
                ArrayList<Map<String,String>> resultValues = new ArrayList<>();
                Element nodeList = (Element)xmlDoc.getElementsByTagName("Kurlar").item(0);

                for (int i=0; i<nodeList.getChildNodes().getLength();i++){
                    Element childelement = (Element) nodeList.getChildNodes().item(i);
                    NodeList nodeChildrenL1 = childelement.getChildNodes();
                    Map<String,String> kur = new HashMap<>();
                    for(int j=0; j<nodeChildrenL1.getLength();j++){
                        Element childElementL1 = (Element) nodeChildrenL1.item(j);
                        String TagName =childElementL1.getTagName();
                        String TagValue =childElementL1.getFirstChild().getNodeValue();
                        kur.put(TagName,TagValue);
                    }
                    resultValues.add(kur);
                }
                ArrayList<MetalDto> listKur = new ArrayList<>();
                for (Map<String,String> kur:resultValues) {
                    listKur.add(new MetalDto());
                    for (Map.Entry<String,String> entry: kur.entrySet()) {
                        String fieldName = entry.getKey();
                        String fieldValue = entry.getValue();
                        switch (fieldName) {
                            case "Kod":
                                listKur.get(listKur.size()-1).Kod = entry.getValue();
                                break;
                            case "Aciklama":
                                listKur.get(listKur.size()-1).Aciklama = entry.getValue();
                                break;
                            case "Alis":
                                listKur.get(listKur.size()-1).Alis =Float.parseFloat(entry.getValue());
                                break;
                            case "Satis":
                                listKur.get(listKur.size()-1).Satis =Float.parseFloat(entry.getValue());
                                break;
                            case "GuncellenmeZamani":
                                SimpleDateFormat simpledateformat = new SimpleDateFormat("d.M.yyyy HH:mm:ss");
                                listKur.get(listKur.size()-1).GuncellenmeZamani = simpledateformat.parse(entry.getValue(),new ParsePosition(0));
                                break;
                        }
                    }
                }

                Collections.sort(listKur, new Comparator<MetalDto>() {
                    @Override
                    public int compare(MetalDto o1, MetalDto o2) {
                        return o1.Aciklama.compareTo(o2.Aciklama);
                    }
                });
                return listKur;
            }
            connection.disconnect();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
        return null;

    }

    public MutableLiveData<MetalRecyclerViewAdapter> setRecyclerAdapter() {
        ArrayList<MetalDto> list = new ArrayList<MetalDto>();
        return metalData;

    }
    
    
}