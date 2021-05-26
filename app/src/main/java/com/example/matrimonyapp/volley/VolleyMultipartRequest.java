package com.example.matrimonyapp.volley;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

@SuppressWarnings("unused")
public class VolleyMultipartRequest extends Request<JSONObject> {
    private final String twoHyphens = "--";
    private final String lineEnd = "\r\n";
    private final String boundary = "apiclient-" + System.currentTimeMillis();

    private Response.Listener<JSONObject> mListener;
    private Response.ErrorListener mErrorListener;
    private Map<String, String> mHeaders;


    public VolleyMultipartRequest(String url, Map<String, String> headers,
                                  Response.Listener<JSONObject> listener,
                                  Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
        this.mHeaders = headers;
    }

    public VolleyMultipartRequest(int method, String url,
                                  Response.Listener<JSONObject> listener,
                                  Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return (mHeaders != null) ? mHeaders : super.getHeaders();
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data;boundary=" + boundary;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            // populate text payload
            Map<String, String> params = getParams();
            if (params != null && params.size() > 0) {
                textParse(dos, params, getParamsEncoding());
            }

            // populate data byte payload
            Map<String, ArrayList<DataPart>> data = getByteData();
            if (data != null && data.size() > 0) {
                dataParse(dos, data);
            }

            // close multipart form data after text and file data
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    protected Map<String, ArrayList<DataPart>> getByteData() throws AuthFailureError {
        return null;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        mErrorListener.onErrorResponse(error);
    }

    private void textParse(DataOutputStream dataOutputStream, Map<String, String> params, String encoding) throws IOException {
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                buildTextPart(dataOutputStream, entry.getKey(), entry.getValue());
            }
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + encoding, uee);
        }
    }

    private void dataParse(DataOutputStream dataOutputStream, Map<String, ArrayList<DataPart>> data) throws IOException {
        for (Map.Entry<String, ArrayList<DataPart>> entry : data.entrySet()) {
            buildDataPart(dataOutputStream, entry.getValue(), entry.getKey());
        }
    }

    private void buildTextPart(DataOutputStream dataOutputStream, String parameterName, String parameterValue) throws IOException {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + parameterName + "\"" + lineEnd);
        //dataOutputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
        dataOutputStream.writeBytes(lineEnd);
        dataOutputStream.writeBytes(parameterValue + lineEnd);
    }

    private void buildDataPart(DataOutputStream dataOutputStream, ArrayList<DataPart> dataFile, String inputName) throws IOException {
        for (int i = 0; i < dataFile.size(); i++) {
            dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
            DataPart dp = dataFile.get(i);
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" +
                    inputName + "\"; filename=\"" + dp.getFileName() + "\"" + lineEnd);
            if (dp.getType() != null && !dp.getType().trim().isEmpty()) {
                dataOutputStream.writeBytes("Content-Type: " + dp.getType() + lineEnd);
            }
            dataOutputStream.writeBytes(lineEnd);

            ByteArrayInputStream fileInputStream = new ByteArrayInputStream(dp.getContent());
            int bytesAvailable = fileInputStream.available();

            int maxBufferSize = 1024 * 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];

            int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dataOutputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            dataOutputStream.writeBytes(lineEnd);
        }
    }

    public class DataPart {
        private String fileName;
        private byte[] content;
        private String type;

        public DataPart() {
        }

        public DataPart(String name, byte[] data) {
            fileName = name;
            content = data;
        }

        public DataPart(String name, byte[] data, String mimeType) {
            fileName = name;
            content = data;
            type = mimeType;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public byte[] getContent() {
            return content;
        }

        public void setContent(byte[] content) {
            this.content = content;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
