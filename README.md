### 一、由来

项目为了普通IT公司需要的各种小工具。
**扫码工具
https://github.com/HDHunter/CompanyQrScanner/releases/download/v1.0.1/company_scan.apk
<img src="./captures/QQ20200116-141252.png">

**app监控
release下载：
https://github.com/HDHunter/CompanyQrScanner/releases/download/v1.0.0/appinfo_monitor.apk
<img src="./captures/QQ20200116-134305.png">


### 二,作用
 
很简单，就是为了公司内部，测试QA人员方便进行二维码扫描。因为普遍的国外，原生Android系统的手机都
不内置带有二维码支持，而网上下载的二维码扫描工具很多都是夹杂广告的。。这个项目纯原生代码实现，
简洁高效，而且方便定制。

### 三，扫码工具效果图
本身来自 [Android-Zing](https://github.com/mylhyl/Android-Zxing) 项目的定制,感谢作者的付出。
zxing提供扫码和生成，cygadapter库提供相册选择，glide提供图片下载

<img src="./captures/Scanner0.png">
<img src="./captures/Scanner1.png">
<img src="./captures/Scanner2.png">


### 四，appMonitor效果图，做了一个应用抽屉。作为一个 module在项目中。为了方便吧。
利用accesibility Service提供支持。
AlarmManager提供刷新。

<img src="./captures/appinfosDesl.png">
<img src="./captures/appinfo2.png">
<img src="./captures/appinfo3.png">