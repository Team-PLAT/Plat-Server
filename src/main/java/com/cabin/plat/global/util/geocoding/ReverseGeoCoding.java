package com.cabin.plat.global.util.geocoding;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

@Component
@RequiredArgsConstructor
public class ReverseGeoCoding {
    private final String API_URL = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc";
    private final String ORDERS_TYPE = "legalcode,roadaddr";
    private final String EMPTY_STRING = "";
    private final String ERROR_STRING = "알 수 없음";

    private final ApiKeyProperties apiKeyProperties;

    public AddressInfo getAddressInfo(double latitude, double longitude) {
        String url = buildUrl(latitude, longitude);
        HttpHeaders headers = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            String responseBody = sendRequest(url, entity);
            return parseResponse(responseBody);
        } catch (HttpStatusCodeException | ResourceAccessException e) {
            System.out.println("HTTP 통신 오류 발생: " + e.getMessage());
            e.printStackTrace();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.out.println("XML 파싱 오류 발생: " + e.getMessage());
            e.printStackTrace();
        } catch(Exception e) {
            System.out.println("알 수 없는 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }

        return new AddressInfo(ERROR_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING);
    }

    private String buildUrl(double latitude, double longitude) {
        return API_URL + "?coords=" + longitude + "," + latitude + "&orders=" + ORDERS_TYPE;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", apiKeyProperties.getClientId());
        headers.set("X-NCP-APIGW-API-KEY", apiKeyProperties.getClientKey());
        return headers;
    }

    private String sendRequest(String url, HttpEntity<String> entity) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }

    private AddressInfo parseResponse(String responseBody) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        InputStream inputStream = new ByteArrayInputStream(responseBody.getBytes());
        Document document = documentBuilder.parse(inputStream);
        document.getDocumentElement().normalize();

        // 주소 없음 (바다)
        String statusCode = getTagValue("code", (Element) document.getElementsByTagName("status").item(0));
        if (statusCode.equals("3")) {
            return new AddressInfo("바다", EMPTY_STRING, EMPTY_STRING, EMPTY_STRING);
        }

        // 주소
        String area1Name = getTagValue("name", (Element) document.getElementsByTagName("area1").item(0));
        String area2Name = getTagValue("name", (Element) document.getElementsByTagName("area2").item(0));
        String area3Name = getTagValue("name", (Element) document.getElementsByTagName("area3").item(0));

        // 건물명
        String buildingName = getTagValue("value", (Element) document.getElementsByTagName("addition0").item(0));

        return new AddressInfo(area1Name, area2Name, area3Name, buildingName);
    }

    private String getTagValue(String tag, Element element) {
        if (element == null) {
            return EMPTY_STRING;
        }

        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        if (node == null) {
            return EMPTY_STRING;
        }

        return node.getNodeValue();
    }
}
