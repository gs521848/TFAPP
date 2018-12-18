package com.example.hookip;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.EnvironmentCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.Map;
import java.util.UUID;

/* compiled from: PhoneInfo */
public class a {
    public static String MANUFACTURER;
    public static String MODEL;
    private static final String TAG = a.class.getSimpleName();
    public static String bba;
    public static String bbb;
    public static String bbc;
    public static String bbd;
    public static String bbe;
    public static String bbf;
    public static String bbg;
    public static String bbh;
    public static String bbi;
    public static String bbj;
    public static boolean bbk;
    public static String bbl = "0";
    public static String bbm;
    public static Context bbn;
    private static int bbo = -1;
    public static String cid;
    public static String phoneNum;
    public static String uuid;
    public static int versionCode;

    private static boolean dj(String str) {
        return TextUtils.isEmpty(str) || "02:00:00:00:00:00".equals(str) || EnvironmentCompat.MEDIA_UNKNOWN.equals(str);
    }

    private static String dk(String str) {
        FileReader fileReader;
        Throwable th;
        FileReader fileReader2 = null;
        String str2 = "";
        try {
            fileReader = new FileReader(str);
            try {
                str2 = b(fileReader);
                try {
                    fileReader.close();
                } catch (IOException e) {
                }
            } catch (Exception e2) {
                if (fileReader != null) {
                    try {
                        fileReader.close();
                    } catch (IOException e3) {
                    }
                }
                return str2;
            } catch (Throwable th2) {
                th = th2;
                fileReader2 = fileReader;
                if (fileReader2 != null) {
                    try {
                        fileReader2.close();
                    } catch (IOException e4) {
                    }
                }
                throw th;
            }
        } catch (Exception e5) {
            fileReader = null;
            if (fileReader != null) {
                try {
					fileReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            return str2;
        } catch (Throwable th3) {
            th = th3;
            if (fileReader2 != null) {
                try {
					fileReader2.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            try {
				throw th3;
			} catch (Throwable e) {
				e.printStackTrace();
			}
        }
        return str2;
    }

    private static String b(Reader reader) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        char[] cArr = new char[4096];
        int read = reader.read(cArr);
        while (read >= 0) {
            stringBuilder.append(cArr, 0, read);
            read = reader.read(cArr);
        }
        return stringBuilder.toString();
    }


    public static String bs(Context context) {
        String macAddress;
        String str;
        Exception e;
        LineNumberReader lineNumberReader;
        String trim;
        Exception exception;
        Throwable th;
        String str2 = "";
        try {
            WifiInfo connectionInfo = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo();
            macAddress = connectionInfo != null ? connectionInfo.getMacAddress() : "";
            str = macAddress;
            str="";
        } catch (Exception e2) {
            e2.printStackTrace();
            str = str2;
        }
        try {
            if (dj(str)) {
                try {
                    macAddress = "";
                    lineNumberReader = new LineNumberReader(new InputStreamReader(Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ").getInputStream()));
                    while (macAddress != null) {
                        try {
                            macAddress = lineNumberReader.readLine();
                            if (macAddress != null) {
                                trim = macAddress.trim();
                                str= macAddress.trim();
                                break;
                            }
                        } catch (Exception e3) {
                        }
                    }
                    trim = str;
                    try {
                        lineNumberReader.close();
                        str2 = trim;
                    } catch (IOException e4) {
                        str2 = trim;
                    } catch (Exception e22) {
                        exception = e22;
                        macAddress = trim;
                        return macAddress;
                    }
                } catch (Exception e5) {
                    lineNumberReader = null;
                    try {
                        if (lineNumberReader != null) {
                            try {
                                lineNumberReader.close();
                                str2 = str;
                            } catch (IOException e6) {
                                str2 = str;
                            }
                            if (dj(str2)) {
                                macAddress = str2;
                            } else {
                                macAddress = dk("/sys/class/net/eth0/address").toUpperCase().substring(0, 17);
                            }
                            if (dj(macAddress)) {
                                return macAddress;
                            }
                            return "";
                        }
                        str2 = str;
                        if (dj(str2)) {
                            macAddress = str2;
                        } else {
                            macAddress = dk("/sys/class/net/eth0/address").toUpperCase().substring(0, 17);
                        }
                        if (dj(macAddress)) {
                            return macAddress;
                        }
                        return "";
                    } catch (Throwable th2) {
                        th = th2;
                        if (lineNumberReader != null) {
                            try {
                                lineNumberReader.close();
                            } catch (IOException e7) {
                            }
                        }
                        try {
							throw th;
						} catch (Throwable e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
                    }
                } catch (Throwable th3) {
                    th = th3;
                    lineNumberReader = null;
                    if (lineNumberReader != null) {
                        lineNumberReader.close();
                    }
                    try {
						throw th;
					} catch (Throwable e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                }
                if (dj(str2)) {
                    macAddress = dk("/sys/class/net/eth0/address").toUpperCase().substring(0, 17);
                } else {
                    macAddress = str2;
                }
                if (dj(macAddress)) {
                    return "";
                }
                return macAddress;
            }
            str2 = str;
            try {
                if (dj(str2)) {
                    macAddress = str2;
                } else {
                    macAddress = dk("/sys/class/net/eth0/address").toUpperCase().substring(0, 17);
                }
            } catch (Exception e222) {
                Exception exception2 = e222;
                macAddress = str2;
                exception = exception2;
                return macAddress;
            }
        } catch (Exception e2222) {
            exception = e2222;
            macAddress = str;
        }
        try {
            if (dj(macAddress)) {
                return macAddress;
            }
            return "";
        } catch (Exception e8) {
            exception = e8;
            return macAddress;
        }
    }

}
