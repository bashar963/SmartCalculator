package com.bashar963.smartcalculator;

import java.math.BigInteger;

public class ProgParser {
    public String decToBin(String x,String state){
        switch (state){
            case "QWORD":
                return Long.toBinaryString(Long.valueOf(x));
            case "DWORD":
                return Integer.toBinaryString(Integer.valueOf(x));
            case "WORD":

                return Integer.toBinaryString(Integer.valueOf(x)&0xFFFF);
            case "BYTE":
                return Integer.toBinaryString(Integer.valueOf(x)&0xFF);
            default:return "0";
        }

    }
    public String decToOct(String x,String state){

        switch (state){
            case "QWORD":
                return Long.toOctalString(Long.valueOf(x));
            case "DWORD":
                return Integer.toOctalString(Integer.valueOf(x));
            case "WORD":
                return Integer.toOctalString(Integer.valueOf(x)&0xFFFF);
            case "BYTE":
                return Integer.toOctalString(Integer.valueOf(x)&0xFF);
            default:return "0";
        }
    }
    public String decToHex(String x,String state){

        switch (state){
            case "QWORD":
                return Long.toHexString(Long.valueOf(x));
            case "DWORD":
                return Integer.toHexString(Integer.valueOf(x));
            case "WORD":
                return Integer.toHexString(Integer.valueOf(x)& 0xFFFF);
            case "BYTE":
                return Integer.toHexString(Integer.valueOf(x)& 0xFF);
            default:return "0";
        }
    }
    public String binToDec(String x,String state){
        switch (state){
            case "QWORD":
                long lo=new BigInteger(x,2).longValue();
                return String.valueOf(lo);
            case "DWORD":
                long i=Long.parseLong(x,2);
                return String.valueOf((int)i);
            case "WORD":
                int in=Integer.parseInt(x,2);
                return String.valueOf((short)in);
            case "BYTE":
                short sh=Short.parseShort(x,2);
                return String.valueOf((byte)sh);
            default:return "0";
        }
    }
    public String octToDec(String x,String state) {
        switch (state) {
            case "QWORD":
                long lo=new BigInteger(x,8).longValue();
                return String.valueOf(lo);
            case "DWORD":
                long i = Long.parseLong(x, 8);
                return String.valueOf((int) i);
            case "WORD":
                int in = Integer.parseInt(x, 8);
                return String.valueOf((short) in);
            case "BYTE":
                short sh = Short.parseShort(x, 8);
                return String.valueOf((byte) sh);
            default:
                return "0";
        }
    }
    public String hexToDec(String x,String state){

        switch (state){
            case "QWORD":
                long lo=new BigInteger(x,16).longValue();
                return String.valueOf(lo);
            case "DWORD":
                long i=Long.parseLong(x,16);
                return String.valueOf((int)i);
            case "WORD":
                int in=Integer.parseInt(x,16);
                return String.valueOf((short)in);
            case "BYTE":
                short sh=Short.parseShort(x,16);
                return String.valueOf((byte)sh);
            default:return "0";
        }


    }

}
