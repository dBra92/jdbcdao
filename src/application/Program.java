package application;

import entities.Seller;
import model.dao.DaoFactory;
import model.dao.SellerDao;

public class Program {

	public static void main(String[] args) {

		SellerDao sellerDao = DaoFactory.createSellerDao();
		Seller seller = sellerDao.findById(2);
		System.out.println(seller);
	}

}
