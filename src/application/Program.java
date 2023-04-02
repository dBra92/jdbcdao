package application;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import entities.Department;
import entities.Seller;
import model.dao.DaoFactory;
import model.dao.SellerDao;

public class Program {

	public static void main(String[] args) {

		Scanner read = new Scanner(System.in);
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		System.out.println("--- Test 1: Seller, findById ---");
		Seller seller = sellerDao.findById(1);
		System.out.println(seller);

		System.out.println("\n--- Test 2: Seller, findByDepartment ---"); // 283
		Department department = new Department(2, null);
		List<Seller> listSell = sellerDao.findByDepartment(department);
		for (Seller sell : listSell) {
			System.out.println(sell);
		}

		System.out.println("\n--- Test 3: Seller, findByDepartment ---"); // 284
		List<Seller> listFA = sellerDao.findAll();
		for (Seller seller2 : listFA) {
			System.out.println(seller2);
		}

		System.out.println("\n--- Test 4: Seller, findByDepartment ---"); // 285
		Seller newSeller = new Seller(1, "Joao", "joao@gmail.com", new Date(), 7000.01, department);
		sellerDao.insert(newSeller);
		System.out.println("Inserted! New Id = " + newSeller.getId());
		
		System.out.println("\n--- Test 5: Seller, update ---");
		Seller seller2 = sellerDao.findById(5);
		seller2.setName("Guilhermino souza santos silva saferino");
		sellerDao.update(seller2);
		System.out.println("Updated completed!");
		
		System.out.println("\n--- Test 6: Seller, deleteByID ---");
		System.out.print("Enter id for delete:");
		int id = read.nextInt();
		sellerDao.deleteById(id);
		System.out.println("Delete completed!");
		read.close();
	}

}
