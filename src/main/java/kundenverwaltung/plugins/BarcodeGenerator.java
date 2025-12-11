package kundenverwaltung.plugins;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

public class BarcodeGenerator
{
    /**
     * this class creates a Barcode for the Customer IDs.
     *
     * @Author Adam Starobrzanski
     * @Date 25.08.2018
     * fileName
     */

  private String pathToTemp;
  /**
   *.
   */
  public String getPathToTemp()
  {
      return pathToTemp;
  }
  /**
   *.
   */
  public void setPathToTemp(String pathToTemp)
  {
      this.pathToTemp = pathToTemp;
  }
    /**
     *.
     */
    public String createBarCode128(String fileName)
    {
        System.out.println(fileName);
        try
        {
            Code128Bean bean = new Code128Bean();
            final int dpi = 160;

            //Configure the barcode generator
            bean.setModuleWidth(UnitConv.in2mm(2.8f / dpi));

            bean.doQuietZone(false);

            File tempBarcodeFile = File.createTempFile("tempBarcode", ".jpg");
            //Open output file
            File outputFile = new File(tempBarcodeFile.getAbsolutePath());

            FileOutputStream out = new FileOutputStream(outputFile);

            BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, "image/x-png", dpi,
                    BufferedImage.TYPE_BYTE_BINARY, false, 0);

            //Generate the barcode
            bean.generateBarcode(canvas, fileName);

            //Signal end of generation
            canvas.finish();

            System.out.println("Bar Code is generated successfully…");

            pathToTemp = tempBarcodeFile.getAbsolutePath();

            out.close();
            tempBarcodeFile.deleteOnExit();
            outputFile.deleteOnExit();

        } catch (IOException ex)
        {
            ex.printStackTrace();
        }

        System.out.println(pathToTemp);
        return pathToTemp;

    }

  /*  public void createBarCode39(String fileName)
    {

        try
        {
            Code39Bean bean39 = new Code39Bean();
            final int dpi = 160;

            //Configure the barcode generator
            bean39.setModuleWidth(UnitConv.in2mm(2.8f / dpi));

            bean39.doQuietZone(false);

            File tempBarcodeFile = File.createTempFile("tempBarcode", ".jpg");
            //Open output file
            File outputFile = new File(tempBarcodeFile.getAbsolutePath());

            FileOutputStream out = new FileOutputStream(outputFile);

            //Set up the canvas provider for monochrome PNG output
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, "image/x-png", dpi,
                    BufferedImage.TYPE_BYTE_BINARY, false, 0);

            //Generate the barcode
            bean39.generateBarcode(canvas, fileName);

            //Signal end of generation
            canvas.finish();

            System.out.println("Bar Code is generated successfully…");
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }*/

    public static void main(String[] str)
    {
        BarcodeGenerator barCodeUtil = new BarcodeGenerator();

        // This will generate Bar-Code 3 of 9 format
      /*  barCodeUtil.createBarCode39("Bc39");*/

        // This will generate Bar-Code 128 format
        barCodeUtil.createBarCode128("Bc128");

    }
}


