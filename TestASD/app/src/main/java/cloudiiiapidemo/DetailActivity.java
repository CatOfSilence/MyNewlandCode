package cloudiiiapidemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.com.newland.cloudiiiapidemo.util.DataCache;
import cn.com.newland.nle_sdk.postDataSet.PostEnum;
import cn.com.newland.nle_sdk.requestEntity.DeviceData;
import cn.com.newland.nle_sdk.requestEntity.DeviceElement;
import cn.com.newland.nle_sdk.responseEntity.DeviceDatas;
import cn.com.newland.nle_sdk.responseEntity.DeviceInfo;
import cn.com.newland.nle_sdk.responseEntity.DeviceState;
import cn.com.newland.nle_sdk.responseEntity.ProjectInfo;
import cn.com.newland.nle_sdk.responseEntity.SensorDataInfoDTO;
import cn.com.newland.nle_sdk.responseEntity.SensorDataPageDTO;
import cn.com.newland.nle_sdk.responseEntity.SensorInfo;
import cn.com.newland.nle_sdk.responseEntity.base.BasePager;
import cn.com.newland.nle_sdk.responseEntity.base.BaseResponseEntity;
import cn.com.newland.nle_sdk.util.NCallBack;
import cn.com.newland.nle_sdk.util.NetWorkBusiness;
import cn.com.newland.nle_sdk.util.Tools;


public class DetailActivity extends BaseActivity {
    private EditText etApiTag;
    private EditText etProjectTag;
    private EditText etMethod;
    private EditText etTimeAgo;
    private EditText etStartTime;
    private EditText etEndTime;
    private EditText etPageIndex;
    private EditText etPageSize;
    private EditText etData;
    private EditText etProjectId;
    private EditText etDeviceId;
    private EditText etKeyWord;
    private EditText etProtocol;
    private EditText etProjectIdOrTag;
    private EditText etDeviceName;
    private EditText etDeviceTag;
    private EditText etDeviceCoordinate;
    private EditText etDeviceDeviceJImg;
    private EditText etSensorName;
    private EditText etTransType;
    private EditText etDataType;
    private EditText etTypeAttrs;
    private EditText etUnit;
    private EditText etPrecision;
    private EditText etOperType;
    private EditText etOperTypeAttrs;
    private EditText etSerialNumber;
    private EditText etHttpIp;
    private EditText etHttpPort;
    private EditText etUserName;
    private EditText etPassword;
    private EditText etValue;
    private EditText etRecordTime;
    private EditText etSort;
    private EditText etNetWorkKind;
    private EditText etGroupBy;
    private EditText etFunc;

    private CheckBox cbOnLine;
    private CheckBox cbShare;
    private CheckBox cbIsTrans;

    private RadioGroup rgDeviceType;

    private TextView tvRequestUrl;
    private TextView tvPostResult;
    private PostEnum postEnum;

    @Override
    protected void onFirst(Bundle saveInstanceState) {
        super.onFirst(saveInstanceState);
        postEnum = (PostEnum) getIntent().getSerializableExtra("apiItem");

    }

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_detail;
    }

    @Override
    protected String setTitle() {
        return postEnum.getTitle();
    }

    @Override
    protected void instantiateView() {
        super.instantiateView();
        etApiTag = findViewById(R.id.apiTag);
        etProjectTag = findViewById(R.id.project_tag);
        etMethod = findViewById(R.id.method);
        etTimeAgo = findViewById(R.id.timeAgo);
        etStartTime = findViewById(R.id.startTime);
        etEndTime = findViewById(R.id.endTime);
        etDeviceId = findViewById(R.id.device_id);
        etPageIndex = findViewById(R.id.PageIndex);
        etPageSize = findViewById(R.id.PageSize);
        etData = findViewById(R.id.Data);
        etKeyWord = findViewById(R.id.key_word);
        etProtocol = findViewById(R.id.Protocol);
        etProjectIdOrTag = findViewById(R.id.ProjectIdOrTag);
        etDeviceName = findViewById(R.id.device_name);
        etDeviceTag = findViewById(R.id.device_tag);
        etDeviceCoordinate = findViewById(R.id.device_Coordinate);
        etDeviceDeviceJImg = findViewById(R.id.device_DeviceImg);
        etSensorName = findViewById(R.id.sensor_name);
        etTransType = findViewById(R.id.TransType);
        etDataType = findViewById(R.id.DataType);
        etTypeAttrs = findViewById(R.id.TypeAttrs);
        etUnit = findViewById(R.id.Unit);
        etPrecision = findViewById(R.id.Precision);
        etOperType = findViewById(R.id.OperType);
        etOperTypeAttrs = findViewById(R.id.OperTypeAttrs);
        etSerialNumber = findViewById(R.id.SerialNumber);
        etHttpIp = findViewById(R.id.HttpIp);
        etHttpPort = findViewById(R.id.HttpPort);
        etUserName = findViewById(R.id.UserName);
        etPassword = findViewById(R.id.Password);
        etValue = findViewById(R.id.Value);
        etRecordTime = findViewById(R.id.RecordTime);
        etSort = findViewById(R.id.Sort);
        etNetWorkKind = findViewById(R.id.NetWorkKind);
        etGroupBy = findViewById(R.id.groupBy);
        etFunc = findViewById(R.id.func);

        cbOnLine = findViewById(R.id.cb_on_line);
        cbShare = findViewById(R.id.cb_is_share);
        cbIsTrans = findViewById(R.id.cb_IsTrans);

        rgDeviceType = findViewById(R.id.device_type);

        etProjectId = findViewById(R.id.project_id);
        tvRequestUrl = findViewById(R.id.requestUrl);
        tvPostResult = findViewById(R.id.postResult);
    }

    @Override
    protected void initViewData() {
        super.initViewData();
        tvRequestUrl.setText(String.format("请求的url地址为: %s%s", DataCache.getBaseUrl(getApplicationContext()), postEnum.getUrl()));
        switch (postEnum) {
            case GET_PROJECT:
                visiableView(etProjectId);
                break;
            case GET_PROJECTS:
                visiableView(etKeyWord, etProjectTag, etNetWorkKind, etPageSize, etStartTime, etEndTime, etPageIndex);
                break;
            case GET_SENSORS:
                visiableView(etProjectId);
                break;
            case GET_DEVICE_NEWS_DATA:
                visiableView(etDeviceId);
                break;
            case GET_DEVICE_STATES:
                visiableView(etDeviceId);
                break;
            case GET_DEVICE:
                visiableView(etDeviceId);
                break;
            case GET_DEVICES:
                visiableView(etKeyWord, etDeviceId, etDeviceTag, cbShare, cbOnLine, etPageIndex, etPageSize, etStartTime, etEndTime);
                break;
            case POST_ADD_DEVICE:
                visiableView(etProtocol, cbIsTrans, etProjectIdOrTag, etDeviceName, etDeviceTag, etDeviceCoordinate, etDeviceDeviceJImg, cbShare);
                break;
            case PUT_UPDATE_DEVICE:
                visiableView(etDeviceId, etProtocol, cbIsTrans, etProjectIdOrTag, etDeviceName, etDeviceTag, etDeviceCoordinate, etDeviceDeviceJImg, cbShare);
                break;
            case DELETE_DEVICE:
                visiableView(etDeviceId);
                break;

            case GET_SENSOR:
                visiableView(etDeviceId, etApiTag);
                break;
            case GET_SENSORS_BY_DEVICE:
                visiableView(etDeviceId, etApiTag);
                break;
            case POST_ADD_SENSOR:
                visiableView(rgDeviceType, etDeviceId, etSensorName, etApiTag, etTransType, etDataType, etTypeAttrs, etUnit, etPrecision, etOperType, etOperTypeAttrs, etSerialNumber, etHttpIp, etHttpPort,
                        etUserName, etPassword);
                break;
            case PUT_UPDATE_SENSOR:
                visiableView(rgDeviceType, etDeviceId, etApiTag, etSensorName, etTransType, etDataType, etTypeAttrs, etUnit, etPrecision, etOperType, etOperTypeAttrs, etSerialNumber, etHttpIp, etHttpPort,
                        etUserName, etPassword);
                break;
            case DELETE_SENSOR:
                visiableView(etDeviceId, etApiTag);
                break;

            case POST_ADD_SENSOR_DATA:
                visiableView(etDeviceId, etValue, etApiTag, etRecordTime);
                break;
            case GET_SENSOR_GROUPING:
                visiableView(etDeviceId, etApiTag, etStartTime, etEndTime, etGroupBy, etFunc);
                break;
            case GET_SENSOR_DATA:
                visiableView(etDeviceId, etApiTag, etMethod, etTimeAgo, etStartTime, etEndTime, etSort, etPageSize, etPageIndex);
                break;
            case POST_CONTROL:
                visiableView(etDeviceId, etApiTag, etData);
                break;


        }
    }

    private void visiableView(View... views) {
        for (View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void registerListener() {
        super.registerListener();
        findViewById(R.id.launch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launch();
            }
        });
    }


    @SuppressLint("SetTextI18n")
    private void launch() {
        NetWorkBusiness netWorkBusiness = new NetWorkBusiness(DataCache.getAccessToken(getApplicationContext()), DataCache.getBaseUrl(getApplicationContext()));
        String projectId = etProjectId.getText().toString();
        String deviceId = etDeviceId.getText().toString();
        String keyword = etKeyWord.getText().toString();
        String projectTag = etProjectTag.getText().toString();
        String deviceTag = etDeviceTag.getText().toString();
        String Protocol = etProtocol.getText().toString();
        String projectIdOrTag = etProjectIdOrTag.getText().toString();
        String networkKind = etNetWorkKind.getText().toString();
        String deviceName = etDeviceName.getText().toString();
        String Coordinate = etDeviceCoordinate.getText().toString();
        String deviceElementName = etSensorName.getText().toString();
        String TransType = etTransType.getText().toString();
        String DataType = etDataType.getText().toString();
        String TypeAttrs = etTypeAttrs.getText().toString();
        String Unit = etUnit.getText().toString();
        String Precision = etPrecision.getText().toString();
        String OperType = etOperType.getText().toString();
        String OperTypeAttrs = etOperTypeAttrs.getText().toString();
        String SerialNumber = etSerialNumber.getText().toString();
        String HttpIp = etHttpIp.getText().toString();
        String HttpPort = etHttpPort.getText().toString();
        String UserName = etUserName.getText().toString();
        String Password = etPassword.getText().toString();
        String value = etData.getText().toString();
        String recordTime = etRecordTime.getText().toString();
        String DeviceImg = etDeviceDeviceJImg.getText().toString();
        String apiTag = etApiTag.getText().toString();
        String method = etMethod.getText().toString();
        String sort = etSort.getText().toString();
        String timeAgo = etTimeAgo.getText().toString();
        String startTime = etStartTime.getText().toString();
        String endTime = etEndTime.getText().toString();
        String pageIndex = etPageIndex.getText().toString();
        String pageSize = etPageSize.getText().toString();
        String groupBy = etGroupBy.getText().toString();
        String func = etFunc.getText().toString();
        final Gson gson = new Gson();
        switch (postEnum) {
            case GET_PROJECT:
                netWorkBusiness.getProject(projectId, new NCallBack<BaseResponseEntity<ProjectInfo>>(getApplicationContext()) {
                    @Override
                    protected void onResponse(BaseResponseEntity<ProjectInfo> response) {
                        Tools.printJson(tvPostResult, gson.toJson(response));
                    }
                });
                break;
            case GET_PROJECTS:
                netWorkBusiness.getProjects(keyword, projectTag, networkKind, pageSize, startTime, endTime, pageIndex, new NCallBack<BaseResponseEntity<BasePager<ProjectInfo>>>(getApplicationContext()) {
                    @Override
                    protected void onResponse(BaseResponseEntity<BasePager<ProjectInfo>> response) {
                        Tools.printJson(tvPostResult, gson.toJson(response));
                    }
                });
                break;
            case GET_SENSORS:
                netWorkBusiness.getAllSensors(projectId, new NCallBack<BaseResponseEntity<List<SensorInfo>>>(getApplicationContext()) {
                    @Override
                    protected void onResponse(BaseResponseEntity<List<SensorInfo>> response) {
                        Tools.printJson(tvPostResult, gson.toJson(response));
                    }
                });
                break;
            case GET_DEVICE_NEWS_DATA:
                netWorkBusiness.getDevicesDatas(deviceId, new NCallBack<BaseResponseEntity<List<DeviceDatas>>>(getApplicationContext()) {
                    @Override
                    protected void onResponse(BaseResponseEntity<List<DeviceDatas>> response) {
                        Tools.printJson(tvPostResult, gson.toJson(response));
                    }
                });
                break;
            case GET_DEVICE_STATES:
                netWorkBusiness.getBatchOnLine(deviceId, new NCallBack<BaseResponseEntity<List<DeviceState>>>(getApplicationContext()) {
                    @Override
                    protected void onResponse(BaseResponseEntity<List<DeviceState>> response) {
                        Tools.printJson(tvPostResult, gson.toJson(response));
                    }
                });
                break;
            case GET_DEVICE:
                netWorkBusiness.getDeviceInfo(deviceId, new NCallBack<BaseResponseEntity<DeviceInfo>>(getApplicationContext()) {
                    @Override
                    protected void onResponse(BaseResponseEntity<DeviceInfo> response) {
                        Tools.printJson(tvPostResult, gson.toJson(response));
                    }
                });
                break;
            case GET_DEVICES:
                netWorkBusiness.getDeviceFuzzy(keyword, deviceId, deviceTag, cbOnLine.isChecked() + "", cbShare.isChecked() + "", "", pageSize, startTime, endTime, pageIndex,
                        new NCallBack<BaseResponseEntity<BasePager<DeviceInfo>>>(getApplicationContext()) {
                            @Override
                            protected void onResponse(BaseResponseEntity<BasePager<DeviceInfo>> response) {
                                Tools.printJson(tvPostResult, gson.toJson(response));
                            }
                        });
                break;
            case POST_ADD_DEVICE:
                DeviceInfo deviceInfo = new DeviceInfo(deviceName, deviceTag, Protocol, Coordinate, cbShare.isChecked() + "", cbIsTrans.isChecked() + "", "", cbOnLine.isChecked() + "",
                        projectIdOrTag, DeviceImg);
                netWorkBusiness.postAddDevice(deviceInfo, new NCallBack<BaseResponseEntity>(getApplicationContext()) {
                    @Override
                    protected void onResponse(BaseResponseEntity response) {
                        Tools.printJson(tvPostResult, gson.toJson(response));
                    }
                });
                break;
            case PUT_UPDATE_DEVICE:
                DeviceInfo deviceInfo1 = new DeviceInfo(deviceName, deviceTag, Protocol, Coordinate, cbShare.isChecked() + "", cbIsTrans.isChecked() + "", "", cbShare.isChecked() + "",
                        projectIdOrTag, DeviceImg);
                netWorkBusiness.updateDevice(deviceId, deviceInfo1, new NCallBack<BaseResponseEntity>(getApplicationContext()) {
                    @Override
                    protected void onResponse(BaseResponseEntity response) {
                        Tools.printJson(tvPostResult, gson.toJson(response));
                    }
                });
                break;
            case DELETE_DEVICE:
                netWorkBusiness.deleteDevice(deviceId, new NCallBack<BaseResponseEntity>(getApplicationContext()) {
                    @Override
                    protected void onResponse(BaseResponseEntity response) {
                        Tools.printJson(tvPostResult, gson.toJson(response));
                    }
                });
                break;
            case GET_SENSOR:
                netWorkBusiness.getSensor(deviceId, apiTag, new NCallBack<BaseResponseEntity<SensorInfo>>(getApplicationContext()) {

                    @Override
                    protected void onResponse(BaseResponseEntity<SensorInfo> arg0) {
                        Tools.printJson(tvPostResult, gson.toJson(arg0));
                    }
                });
            case GET_SENSORS_BY_DEVICE:
                netWorkBusiness.getSensors(deviceId, apiTag, new NCallBack<BaseResponseEntity<List<SensorInfo>>>(getApplicationContext()) {
                    @Override
                    protected void onResponse(BaseResponseEntity<List<SensorInfo>> response) {
                        Tools.printJson(tvPostResult, gson.toJson(response));
                    }
                });
                break;
            case POST_ADD_SENSOR:
                DeviceElement deviceElement = null;
                switch (rgDeviceType.getCheckedRadioButtonId()) {
                    case R.id.rb_sensor:
                        deviceElement = new DeviceElement.SensorDeviceElement(apiTag, deviceElementName, TransType, DataType, TypeAttrs, Unit, Precision);
                        break;
                    case R.id.rb_actuator:
                        deviceElement = new DeviceElement.ActuatorDeviceElement(apiTag, deviceElementName, TransType, DataType, TypeAttrs, OperType, OperTypeAttrs, SerialNumber);
                        break;
                    case R.id.rb_camera:
                        deviceElement = new DeviceElement.CameraDeviceElement(apiTag, deviceElementName, TransType, DataType, TypeAttrs, HttpIp, HttpPort, UserName, Password);
                        break;
                }
                netWorkBusiness.addSensor(deviceId, deviceElement, new NCallBack<BaseResponseEntity>(getApplicationContext()) {
                    @Override
                    protected void onResponse(BaseResponseEntity response) {
                        Tools.printJson(tvPostResult, gson.toJson(response));
                    }
                });
                break;
            case PUT_UPDATE_SENSOR:
                switch (rgDeviceType.getCheckedRadioButtonId()) {
                    case R.id.rb_sensor:
                        DeviceElement.SensorDeviceElement sensorDeviceElement = new DeviceElement.SensorDeviceElement(apiTag, deviceElementName, TransType, DataType, TypeAttrs, Unit,
                                Precision);
                        netWorkBusiness.updateSensor(deviceId, apiTag, sensorDeviceElement, new NCallBack<BaseResponseEntity>(getApplicationContext()) {
                            @Override
                            protected void onResponse(BaseResponseEntity response) {
                                Tools.printJson(tvPostResult, gson.toJson(response));
                            }
                        });
                        break;
                    case R.id.rb_actuator:
                        DeviceElement.ActuatorDeviceElement actuatorDeviceElement = new DeviceElement.ActuatorDeviceElement(apiTag, deviceElementName, TransType, DataType, TypeAttrs,
                                OperType, OperTypeAttrs, SerialNumber);

                        netWorkBusiness.updateActuator(deviceId, apiTag, actuatorDeviceElement, new NCallBack<BaseResponseEntity>(getApplicationContext()) {
                            @Override
                            protected void onResponse(BaseResponseEntity response) {
                                Tools.printJson(tvPostResult, gson.toJson(response));
                            }
                        });
                        break;
                    case R.id.rb_camera:
                        DeviceElement.CameraDeviceElement cameraDeviceElement = new DeviceElement.CameraDeviceElement(apiTag, deviceElementName, TransType, DataType, TypeAttrs, HttpIp,
                                HttpPort, UserName, Password);
                        netWorkBusiness.updateCamera(deviceId, apiTag, cameraDeviceElement, new NCallBack<BaseResponseEntity>(getApplicationContext()) {
                            @Override
                            protected void onResponse(BaseResponseEntity response) {
                                Tools.printJson(tvPostResult, gson.toJson(response));
                            }
                        });
                        break;
                }
                break;
//
            case DELETE_SENSOR:
                netWorkBusiness.deleteDeviceElement(deviceId, apiTag, new NCallBack<BaseResponseEntity>(getApplicationContext()) {
                    @Override
                    protected void onResponse(BaseResponseEntity response) {
                        Tools.printJson(tvPostResult, gson.toJson(response));
                    }
                });
                break;
            case POST_ADD_SENSOR_DATA:
                List<DeviceData.DatasDTO> datasDTOS = new ArrayList<>();
                List<DeviceData.PointDTO> pointDTOS = new ArrayList<>();
                pointDTOS.add(new DeviceData.PointDTO(value, recordTime));
                datasDTOS.add(new DeviceData.DatasDTO(apiTag, pointDTOS));
                DeviceData deviceData = new DeviceData(datasDTOS);
                netWorkBusiness.addSensorData(deviceId, deviceData, new NCallBack<BaseResponseEntity>(getApplicationContext()) {
                    @Override
                    protected void onResponse(BaseResponseEntity response) {
                        Tools.printJson(tvPostResult, gson.toJson(response));
                    }
                });
                break;
            case GET_SENSOR_GROUPING:
                netWorkBusiness.getSensorDataGrouping(deviceId, apiTag, groupBy, func, startTime, endTime, new NCallBack<BaseResponseEntity<SensorDataInfoDTO>>(getApplicationContext()) {
                    @Override
                    protected void onResponse(BaseResponseEntity<SensorDataInfoDTO> response) {
                        Tools.printJson(tvPostResult, gson.toJson(response));
                    }
                });
                break;
            case GET_SENSOR_DATA:
                netWorkBusiness.getSensorData(deviceId, apiTag, method, timeAgo, startTime, endTime, sort, pageSize, pageIndex, new NCallBack<BaseResponseEntity<SensorDataPageDTO>>(getApplicationContext()) {
                    @Override
                    protected void onResponse(BaseResponseEntity<SensorDataPageDTO> response) {
                        Tools.printJson(tvPostResult, gson.toJson(response));
                    }
                });
                break;
            case POST_CONTROL:
                netWorkBusiness.control(deviceId, apiTag, value, new NCallBack<BaseResponseEntity>(getApplicationContext()) {
                    @Override
                    protected void onResponse(BaseResponseEntity response) {
                        Tools.printJson(tvPostResult, gson.toJson(response));
                    }
                });
                break;
        }
    }
}