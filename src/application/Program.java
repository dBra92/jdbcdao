package application;

import java.util.Date;

import entities.Department;
import entities.Seller;
import model.dao.DaoFactory;
import model.dao.SellerDao;

public class Program {

	public static void main(String[] args) {

		Department dp = new Department(1, "Books");
		Seller sell = new Seller(1, "Jack", "jack@ggmail.com", new Date(29 / 1 / 1999), 4000.0, dp);
		SellerDao sellerDao = DaoFactory.createSellerDao();
		System.out.println(sell);
	}

}
