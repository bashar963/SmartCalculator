package com.bashar963.smartcalculator;


import org.javia.arity.Symbols;
import org.javia.arity.SyntaxException;


import java.text.DecimalFormat;


public class ArityParser  {
    private double result;
    public String parse(String expression) {
        try {

            Symbols symbols = new Symbols();
            if (expression.contains("root")){
                while (expression.contains("root")){
                    String root=expression.substring(expression.indexOf("y"),expression.indexOf(")"));
                    String num[]= root.split(" ");
                    expression=expression.replaceFirst("y.*?\\)", nthroot(num[1],num[3]));
                }
            }else {
                result = symbols.eval(expression);
            }
            result = symbols.eval(expression);
            if (!(result%1==-0)){
               // if (result>0.0000000001) return new DecimalFormat("#.######E00").format(result);
                //else
                    return new DecimalFormat("#.########################").format(result);
            }else {
                if (result>Long.MAX_VALUE) return new DecimalFormat("######E00").format(result);
                else if (result<Long.MIN_VALUE) return new DecimalFormat("######E00").format(result);
                else return String.valueOf((long)result);
            }

        } catch (SyntaxException ex) {
            return "ERROR: " + ex.getMessage();
        }
    }
private String nthroot(String x, String y){
    Double n1=Double.valueOf(x);
    Double n2 = Double.valueOf(y);
    Double res=Math.pow(n1,1/n2);
    return  String.valueOf(res);
}
}