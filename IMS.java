import java.sql.*;
import java.util.*;
class Ims
{
static Connection con=null;
static Scanner sc=new Scanner(System.in);
static
{
    try
    {
      Class.forName("com.mysql.jdbc.Driver");
      con=DriverManager.getConnection("jdbc:mysql://localhost:3306/ims","root","");
      if(con!=null)
      {
        System.out.println("Connection open....");
      }
    }
    catch(Exception e)
    {
      System.out.println(e);
    }
}
public static void CS() throws Exception
{
        new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
}
public static void Add_product() throws Exception
{
  try{
    System.out.print("\t\t\tEnter Product ID: ");
    int pid=sc.nextInt();
    System.out.print("\t\t\tEnter Product Name: ");
    sc.nextLine();
    String pname=sc.nextLine();

    System.out.print("\t\t\tEnter Purchase Price: ");
    float prprice=sc.nextFloat();
    System.out.print("\t\t\tEnter the Sale Price: ");
    float sprice=sc.nextFloat();
    System.out.print("\t\t\tEnter Product Quantity: ");
    int pquantity=sc.nextInt();

    PreparedStatement pst=con.prepareStatement("insert into product values(?,?,?,?,?)");
    pst.setInt(1,pid);
    pst.setString(2,pname.toLowerCase());
    pst.setFloat(3,prprice);
    pst.setFloat(4,sprice);
    pst.setInt(5,pquantity);

          if(pst.executeUpdate()>0)
          {
              System.out.println("\t\t\tData Inserted....");
          }
          else
          {
              System.out.println("\t\t\tData not inserted...");
          }
  }catch(Exception e){
    System.out.println("\t\t\tThis Id is Already existed!!!");
  }


}

public static void deleteProduct() throws Exception
{
    try{
      System.out.print("\t\t\tEnter product id:");
      int pid=sc.nextInt();
      PreparedStatement pst=con.prepareStatement("select * from product");
      ResultSet rs=pst.executeQuery();

      while(rs.next())
      {
      pst=con.prepareStatement("delete  from product  where productId=?");
      pst.setInt(1,pid);
      }

      if(pst.executeUpdate()>0){
        System.out.println("\t\t\tRecord Deleted successfully");
            }
      else{
        System.out.println("\t\t\tRecord not Found!!!");
      }
    }catch(Exception e){
      System.out.println("\t\t\tCan't be deleted!!!");
    }

}


public static void Removesale() throws Exception
{

      System.out.print("\t\t\tEnter sale id:");
      int sid=sc.nextInt();
      PreparedStatement pst=con.prepareStatement("select * from sale");
      ResultSet rs=pst.executeQuery();

      int pid=0,qty=0;
      while(rs.next())
      {
      pst=con.prepareStatement("delete  from sale  where saleid=?");
      pst.setInt(1,sid);

      pid = rs.getInt(2);
      qty = rs.getInt(5);
      }

      PreparedStatement newpst = con.prepareStatement("select productQty from product where productId=?");
      newpst.setInt(1,pid);

      ResultSet newrs = newpst.executeQuery();

      while(newrs.next())
      {
        int oldQty = newrs.getInt(1);

        newpst=con.prepareStatement("update  product set productQty=? where productId=?");
        newpst.setInt(1,oldQty+qty);
        newpst.setInt(2,pid);

        newpst.executeUpdate();
      }


      if(pst.executeUpdate()>0){
        System.out.println("\t\t\tRecord Deleted successfully");
            }
      else{
        System.out.println("\t\t\tRecord not Found!!!");
      }

}


public static void viewProduct() throws Exception
{
	PreparedStatement pst=con.prepareStatement("select * from product");
	ResultSet rs=pst.executeQuery();

	System.out.println("Product ID\tProduct Name\t\tPurchase Price\t\tSale Price\t\tQty");

	while(rs.next())
	{
	int pid=rs.getInt(1);
	String pname=rs.getString(2);
	float price=rs.getFloat(3);
	float sprice=rs.getFloat(4);
	int pqty=rs.getInt(5);

  System.out.printf("%-18d%-28s%-22.2f%-20.2f%-5d%n", pid,pname,price,sprice,pqty);
	}
}
public static void updateProductQty() throws Exception
{
		System.out.print("\t\t\tEnter product id:");
		int pid=sc.nextInt();
		PreparedStatement pst=con.prepareStatement("select * from product where productId=?");
    pst.setInt(1,pid);
		ResultSet rs = pst.executeQuery();
    int oldQty=0;
    int flag = 0;
    while(rs.next())
    {
       oldQty = rs.getInt(5);
       flag = 1;
    }
    if(flag==1)
    {
          pst=con.prepareStatement("update  product set productQty=? where productId=?");
      		pst.setInt(2,pid);

      		System.out.print("\t\t\tEnter the product quantity:");
      		int pdq=sc.nextInt();
      		pst.setInt(1,(pdq+oldQty));

      		if(pst.executeUpdate()>0)
      		{

      			System.out.println("\t\t\tRecord Updated");
      		}
      		else
      		{
      			System.out.println("\t\t\tRecord not Updated");
      		}
    }else{
            System.out.println("\t\t\tRecord not Found.....!!!");
    }



}
public static void viewStock()throws Exception
{
	PreparedStatement pst=con.prepareStatement("select * from product");
	ResultSet rs=pst.executeQuery();

	System.out.println("Product ID\tProduct Name\t\t\tQty");

	while(rs.next())
	{
	int pid=rs.getInt(1);
	String pname=rs.getString(2);
	int pqty=rs.getInt(5);

	//System.out.println(pid+"\t\t"+pname+"\t\t"+price+"\t\t"+sprice+"\t\t"+pqty);
  System.out.printf("%-18d%-30s%-5d%n", pid,pname,pqty);
	}
}

public static void insertproductsaledetail() throws Exception
{
     try{
      System.out.print("\t\t\tEnter SALE ID: ");
      int id1=sc.nextInt();
      System.out.print("\t\t\tEnter PRODUCT Name: ");
      sc.nextLine();
      String name=sc.nextLine();



      PreparedStatement getpst=con.prepareStatement("select * from product where productName=?");
      getpst.setString(1,name.toLowerCase());
  		ResultSet rs = getpst.executeQuery();
      int getQty = 0;
      int id2 = 0;
      float ssp = 0;
      int flag =0;
      while(rs.next())
      {
         id2 = rs.getInt(1);
         ssp = rs.getFloat(4);
         getQty = rs.getInt(5);

         flag = 1;
      }


      if(flag==1)
      {
            System.out.print("\t\t\tEnter Sale QUANTITY: ");
            int qty=sc.nextInt();
            sc.nextLine();


            getpst=con.prepareStatement("update  product set productQty=? where productId=?");
        		getpst.setInt(2,id2);
            getpst.setInt(1,(getQty-qty));
            if((getQty-qty)>=0 && qty>0)
            {


              System.out.print("\t\t\tEnter DATE As DD/MM/YY: ");
              String date1=sc.nextLine();
              String date = modifiedDate(date1);



              getpst.executeUpdate();



            PreparedStatement pst=con.prepareStatement("insert into sale values(?,?,?,?,?)");
            pst.setInt(1,id1);
            pst.setInt(2,id2);
            pst.setFloat(3,ssp);
            pst.setInt(5,qty);
      	    java.util.Date d=new java.util.Date(date);
            java.sql.Date d1=new java.sql.Date(d.getYear(),d.getMonth(),d.getDate());
            pst.setDate(4,d1);  //Parameterized query

            if(pst.executeUpdate()>0)
            {

                System.out.println("\t\t\tNew Sale Details Inserted...");
            }
             else
             {

                System.out.println("\t\t\tData not Inserted...");
             }

           }else{
             System.out.println("\t\t\t[Sale Quantity is greater than the Quantity in Stock]");
             sc.nextLine();
             Ims.CS();
             insertproductsaledetail();
           }
      }else{
              System.out.println("\t\t\t[Please Enter correct name]");
              sc.nextLine();
              Ims.CS();
              insertproductsaledetail();
      }

   }catch(Exception e){

        System.out.println(e);
      }
}



public static void updateProductSaleDetail() throws Exception
{
	try{
       System.out.print("\t\t\tEnter Sale ID: ");
       int saleid1=sc.nextInt();
       PreparedStatement pst=con.prepareStatement("select * from sale where saleid=?");
       pst.setInt(1,saleid1);
       ResultSet rs=pst.executeQuery();
       int missQty=0,id1;
       float price =0;
       int newQty=0;
       if(rs.next())
       {
              id1=rs.getInt(2);
              missQty = rs.getInt(5);
              price = rs.getFloat(3);

              pst=con.prepareStatement("update sale set price=?,date=?,saleQty=? where saleid=? ");

              System.out.print("\t\t\tEnter Product QUANTITY: ");
              int qty = sc.nextInt();
              newQty=(qty-missQty);
             	System.out.print("\t\t\tEnter DATE As DD/MM/YY :");
						 	sc.nextLine();
             	String date=sc.nextLine();

              String dat = modifiedDate(date);
              pst.setFloat(1,price);
             	pst.setInt(4,saleid1);
             	pst.setInt(3,qty);
	     				java.util.Date d=new java.util.Date(dat);
             	java.sql.Date d1=new java.sql.Date(d.getYear(),d.getMonth(),d.getDate());
             	pst.setDate(2,d1);  //Parameterized query

              PreparedStatement pst1=con.prepareStatement("select * from product where productId=?");
              pst1.setInt(1,id1);
              ResultSet rs_pro= pst1.executeQuery();
              int oldProQty=0;
              while(rs_pro.next())
              {
                oldProQty=rs_pro.getInt(5);
              }

              pst1=con.prepareStatement("update product set productQty=? where productId=?");
              pst1.setInt(2,id1);
              pst1.setInt(1,(oldProQty-newQty));

              if((oldProQty-newQty)>0)
              {
                pst1.executeUpdate();


                  if(pst.executeUpdate()>0)
                  {
                      System.out.println("\t\t\tRecord UPDATED!!!!!!!");
                    }
                  else
                    {
                       System.out.println("\t\t\tRecord not UPDATED!!!!!!!");
                    }
                }else{
                System.out.println("\t\t\t[Sale Quantity is greater than the Quantity in Stock]");
                  }


            }
          else
           {
             System.out.println("\t\t\tRecord not Found !!!!!!!");
           }

         }

      catch(SQLException e){System.out.println(e);}

}
public static void viewSaleProduct() throws Exception
{
    System.out.print("\t\t\tEnter DATE As DD/MM/YY :");
    String date1 = sc.nextLine();

    String date = modifiedDate(date1);

    java.util.Date d=new java.util.Date(date);
    java.sql.Date d1=new java.sql.Date(d.getYear(),d.getMonth(),d.getDate());

    PreparedStatement pst = con.prepareStatement("select * from sale where date=?");
    pst.setDate(1,d1);
    ResultSet rs=pst.executeQuery();
    System.out.println();
    System.out.println();


        System.out.println("Sale ID\t\tProduct ID\t\tProduct Name\t\tPrice\t\tSale Qty");
        while(rs.next())
        {
          int id = rs.getInt(1);
          int pid = rs.getInt(2);
          float price = rs.getFloat(3);
          int qty = rs.getInt(5);

          PreparedStatement pst1 = con.prepareStatement("select productName from product where productId=?");
          pst1.setInt(1,pid);
          ResultSet rs1 = pst1.executeQuery();

          String productName="";
          while(rs1.next())
          {
            productName = rs1.getString(1);
          }


          System.out.printf("%-18d%-22d%-24s%-18.2f%-5d%n", id,pid,productName,price,qty);
        }





}

public static void profitAndLoss() throws Exception{

          System.out.println("\t\t\t\t\tPlease Select Date \"(DD/MM/YY)\" ");
          System.out.print("\t\t\t Enter Start Date: ");
          String date = sc.nextLine();

          String date1 = modifiedDate(date);

          java.util.Date sd=new java.util.Date(date1);
          java.sql.Date sd1=new java.sql.Date(sd.getYear(),sd.getMonth(),sd.getDate());

          System.out.print("\t\t\t Enter End Date: ");
          String olddate2 = sc.nextLine();

          String date2 = modifiedDate(olddate2);

          java.util.Date ed=new java.util.Date(date2);
          java.sql.Date ed2=new java.sql.Date(ed.getYear(),ed.getMonth(),ed.getDate());

          PreparedStatement pst = con.prepareStatement("select * from sale where date>=? and date<=?");
          pst.setDate(1,sd1);
          pst.setDate(2,ed2);

          ResultSet rs = pst.executeQuery();

          System.out.println("Sale ID\t\tProduct ID\t\tProduct Name\t\tPrice\t\tSale Qty\t\tProfit");

          float sum=0;
          while(rs.next())
          {
            int id = rs.getInt(1);
            int pid = rs.getInt(2);
            float sprice = rs.getFloat(3);
            int qty = rs.getInt(5);

            float totalSalePrice = sprice*qty;

            PreparedStatement pst1 = con.prepareStatement("select * from product where productId=?");
            pst1.setInt(1,pid);
            ResultSet rs1 = pst1.executeQuery();

            String productName="";
            float purchasePrice;
            float totalCostPrice=0;
            while(rs1.next())
            {
              productName = rs1.getString(2);
              purchasePrice = rs1.getFloat(3);

              totalCostPrice = purchasePrice*qty;

            }

            float profit = totalSalePrice-totalCostPrice;
            sum+=profit;


            System.out.printf("%-18d%-22d%-24s%-18.2f%-22d%-5.2f%n", id,pid,productName,sprice,qty,profit);
          }
          System.out.println();
          System.out.println();
          System.out.println();

          System.out.print("\t\t\t\t\tTotal Profit = "+sum);


    }

    public static void Restore() throws Exception
    {
      PreparedStatement pst = con.prepareStatement("delete from sale");


      PreparedStatement productpst = con.prepareStatement("delete from product");


      System.out.print("\t\t\t\t\tEnter your Password:");
      String password = sc.nextLine();
      if(password.compareTo("3306")==0)
      {
        pst.executeUpdate();
        productpst.executeUpdate();

        System.out.print("\t\t\t\t\tSoftware is Restore to its Original state!!!");
        sc.nextLine();
      }else{
        System.out.println("\t\t\t\t\tIncorrect Password!!!!");
        sc.nextLine();
      }


    }

    private static String modifiedDate(String date)
    {
      String newDate;
      if(date.charAt(2) == '/' && date.charAt(5) == '/')
      {
         newDate = date.substring(6,10)+"/"+date.substring(3,5)+"/"+date.substring(0,2);

      }else{
        newDate = date;
      }
        return newDate;

    }

}

class Main
{
  public static void main(String[] args) throws Exception
  {
    try{
      Scanner sc=new Scanner(System.in);

      Ims.CS();

      System.out.println();
      System.out.println();
      System.out.println();
      System.out.println();
      System.out.println();
      System.out.println();
      System.out.println();
      System.out.println("   ------------------------------------------------------------------------------------------------------------------");
      System.out.println("   |                                                                                                                |");
      System.out.println("   |                                      WELCOME TO INVENTORY MANAGEMENT SYSTEM                                    |");
      System.out.println("   |                                                                                                                |");
      System.out.println("   |                                                                                                                |");
      System.out.println("   |                                          PRESS  ENTER KEY TO LOGIN                                             |");
      System.out.println("   |                                                                                                                |");
      System.out.println("   |                                                                                                                |");
      System.out.println("   ------------------------------------------------------------------------------------------------------------------");
  	  sc.nextLine();
      Ims.CS();

      while(true){
              Ims.CS();

              System.out.println("\t\t\t==================================================================");
              System.out.println("\t\t\t1.Manage Stock");
              System.out.println();
              System.out.println("\t\t\t2.Manage Sale");
              System.out.println();
              System.out.println("\t\t\t3.Manage Product");
              System.out.println();
              System.out.println("\t\t\t4.View Profit Details");
              System.out.println();
              System.out.println("\t\t\t5.Reset Database");
              System.out.println();
              System.out.println("\t\t\t6.Exit");
              System.out.println();

              System.out.println("\t\t\t===================================================================");
              System.out.print("\t\t\tEnter Your choice :");
              int ch=sc.nextInt();
              Ims.CS();
              switch(ch)
              {
                case 1:
                      System.out.println("\t\t\t===================================================================");
                      System.out.println("\t\t\t1.Update Product Quantity in Stock");
                      System.out.println();
                      System.out.println("\t\t\t2.View Stock");
                      System.out.println();
                      System.out.println("\t\t\t3.To Main Menu");

                      System.out.println("\t\t\t===================================================================");

                      System.out.print("\t\t\tEnter Your choice :");
                      int ch1=sc.nextInt();
                      Ims.CS();
                      switch(ch1)
                      {
                          case 1:
                              Ims.updateProductQty();
                              sc.nextLine();
                							System.out.println("\n\t\t\tPress Enter to continue....");
                							sc.nextLine();
                              continue;
                          case 2:
                              Ims.viewStock();
                              sc.nextLine();
                							System.out.println("\n\t\t\tPress Enter to continue....");
                							sc.nextLine();
                              continue;
                          case 3:
                              continue;
                      }

                case 2:
                      System.out.println("\t\t\t===================================================================");
                      System.out.println("\t\t\t1.Insert Product Sale Detail");

                      System.out.println();
                      System.out.println("\t\t\t2.Upadate Sale ");

                      System.out.println();
                      System.out.println("\t\t\t3.view Sale");
                      System.out.println();
                      System.out.println("\t\t\t4.Remove sale Record");
                      System.out.println();
                      System.out.println("\t\t\t5.Main Menu");
                      System.out.println();
                      System.out.println("\t\t\t===================================================================");
                      System.out.print("\t\t\tEnter your choice:");
                      int ch2=sc.nextInt();

                      Ims.CS();
                      switch(ch2)
                      {
                          case 1:
                              Ims.insertproductsaledetail();
                              sc.nextLine();
                							System.out.println("\n\t\t\tPress Enter to continue....");
                							sc.nextLine();
                              continue;
                          case 2:
                              Ims.updateProductSaleDetail();
                              sc.nextLine();
                							System.out.println("\n\t\t\tPress Enter to continue....");
                							sc.nextLine();
                              continue;
                          case 3:
                              Ims.viewSaleProduct();
                              sc.nextLine();
                							System.out.println("\n\t\t\tPress Enter to continue....");
                							sc.nextLine();
                              continue;
                         case 4:
                               Ims.Removesale();
                               sc.nextLine();
                               System.out.println("\n\t\t\tPress Enter to continue....");
                               sc.nextLine();
                               continue;
                          case 5:
                              continue;
                     }

                case 3:
                      System.out.println("\t\t\t===================================================================");
                      System.out.println("\t\t\t1.Add New Product");
                      System.out.println();
                      System.out.println("\t\t\t2.View all Product ");
                      System.out.println();
                      System.out.println("\t\t\t3.Remove Product");
                      System.out.println();
                      System.out.println("\t\t\t4.To Main Menu");
                      System.out.println("\t\t\t===================================================================");
                      System.out.print("\t\t\tEnter your choice:");
                      int ch3 = sc.nextInt();
                        Ims.CS();
                        switch(ch3)
                        {
                          case 1:
                                Ims.Add_product();
                                sc.nextLine();
                  							System.out.println("\n\t\t\tPress Enter to continue....");
                  							sc.nextLine();
                                continue;
                          case 2:
                                Ims.viewProduct();
                                sc.nextLine();
                  							System.out.println("\n\t\t\tPress Enter to continue....");
                  							sc.nextLine();
                                continue;
                          case 3:
                                Ims.deleteProduct();
                                //System.out.println("\t\t\tThis function is under maintanance....");
                                sc.nextLine();
                  							System.out.println("\n\t\t\tPress Enter to continue....");
                  							sc.nextLine();
                                continue;
                          case 4:
                                continue;
                        }
                case 4:
                      System.out.println("\t\t\t===================================================================");
                      System.out.println("\t\t\t1.View profit details of a product");
                      System.out.println();
                      System.out.println("\t\t\t2.To Main Menu");
                      System.out.println("\t\t\t===================================================================");
                      System.out.print("\t\t\tEnter your choice:");
                      int ch4=sc.nextInt();

                      Ims.CS();
                      switch(ch4){

            							case 1:
                                Ims.profitAndLoss();
                                sc.nextLine();
                                System.out.println("\n\t\t\t\t\tPress Enter to continue....");
                                sc.nextLine();
                                continue;

            							case 2:
                                continue;
            						   }

                case 5:
                      Ims.Restore();
                      continue;

                case 6:
                      Ims.con.close();
                      System.exit(0);


              }
      }
    }catch(Exception e){}
  }

}
