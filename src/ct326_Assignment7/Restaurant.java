/*
    Name: Enda Kilgarriff
    Student ID: 17351606
 */

package ct326_Assignment7;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Restaurant {

	public Restaurant() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		List<String> orderList = new ArrayList<>();
        File file = new File("C:/TextFiles/orderList");
        try{
            Scanner sc = new Scanner(file);

            while(sc.hasNextLine()){
                orderList.add(sc.nextLine());
            }
            sc.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } 

        for(String i: orderList) {
            System.out.println(i);
        }
        
        
	}

}
