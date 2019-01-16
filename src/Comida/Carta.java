package Comida;

import java.sql.Statement;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Logger;

import javax.swing.JOptionPane;


import jdk.nashorn.internal.parser.TokenStream;
import sun.security.jgss.TokenTracker;

public class Carta {
	/* Clase carta,
	 * objetivo leer y escribir en la BD   */
	
	public HashSet<Comida> comidas;
	public HashSet<Bebida>bebidas;
	
	
	public Carta(HashSet<Comida> comidas, HashSet<Bebida> bebidas) {
		super();
		this.comidas = comidas;
		this.bebidas = bebidas;
	}
	public Carta(Carta c) {
		
		this.comidas = c.comidas;
		this.bebidas = c.bebidas;
	}
	public Carta() {
		super();
		this.comidas = new HashSet<>();
		this.bebidas = new HashSet<>();
	}


	public HashSet<Comida> getComidas() {
		return comidas;
	}
	public void setComidas(HashSet<Comida> comidas) {
		this.comidas = comidas;
	}
	public HashSet<Bebida> getBebidas() {
		return bebidas;
	}
	public void setBebidas(HashSet<Bebida> bebidas) {
		this.bebidas = bebidas;
	}
	
	//A�adir comida/bebida
	public void addComida(Comida c) {
		this.comidas.add(c);
		
	}
	public void addBebida(Bebida b) {
		this.bebidas.add(b);
	}
	
	
	
	
	//introducimos todos los datos en la BD
	//primero habra que saber si ya existe para hacer un insert o un update
	public void guardarCarta() {
		boolean resultBebidas= false;
		boolean	resultComidas= false; 
		
		Connection conn = BD.initBD();
		
		
		try {
			Statement st = conn.createStatement();
			for (Bebida bebida : bebidas) {
			//creamos una conexion y mediante el metodo siguiente introducimos los valores en la BD
			if(BD.Select(st, "cod = "+ bebida.id, "bebida")) {//comprobamos si existe esta comida o no para actualizarla
				resultBebidas = BD.bebidaUpdate(st, bebida);//existe y actualizamos
			}else{
				resultBebidas  = BD.Insert(st,  bebida.toString() , "bebida");//no existe y creamos
			}
				
			}
			
			 for (Comida comida : comidas) {
				 if(BD.Select(st, "cod = "+comida.id,"comida")) {//comprobamos si existe esta comida
					 resultComidas = BD.comidaUpdate(st, comida);//existe y actualizamos					 
				 }else{
					 resultComidas = BD.Insert(st, comida.toString(), "comida");//no existe y creamos
				 }
			}
			 
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}			
		
		
	
		
	}
	
	//leer en fichero de texto
//		File archivo = null;
//		FileReader fr = null;
//		BufferedReader br = null;
//		ArrayList<String> todo = new ArrayList<>();
//		try {//buscar el arrchivo
//			archivo = new File("src/ficherosDeTexto/carta.txt");
//			fr = new FileReader(archivo);//path
//			System.out.println("encontrado");
//			br = new BufferedReader(fr);
//			String linea ;
////
//			   while((linea=br.readLine())!=null) {//mientras no este vacio el fichero
//				   String[]tockens = linea.split(","); //separamos en tockens las distintas partes
//				   if( tockens.length == 6) {//bebida completada
//					   //es bebida
//					   Bebida b = new Bebida();
//					   b.id = Integer.parseInt(tockens[0]);
//					   b.nombre = tockens[1];
//					   b.precio = Double.parseDouble(tockens[2]);
//					   b.setDescripcion(tockens[3]);
//					   b.setmL(Integer.parseInt(tockens[4]));
//					   b.setAlcoholica(Boolean.parseBoolean(tockens[5]));
//					  this.bebidas.add(b);
//				   }else {
//					   //es comida
//					   Comida c = new Comida();
//					   c.id = Integer.parseInt(tockens[0]) ;
//					   c.nombre = tockens[1];
//					   c.precio =  Double.parseDouble(tockens[2]) ;
//					  
//				   }
//				   
//		            
//			   	}
//				//crea el popup
//			
//					 JOptionPane.showMessageDialog(null, "carga completada");
//				
//				
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println("no encontrado");
//		}finally {
//			try {
//				 if( null != fr ){   
//		               fr.close();     
//		            } 
//				System.out.println("cerrando");
//				System.out.println(todo.toString());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
		
	
	
	public void cargarCarta() {
		String sentSQL= "";
		Connection conn = BD.initBD();
		try {
			Statement st = conn.createStatement();
			//cargamos las bebidas y las guardamos
			sentSQL = "select * from bebida";
			ResultSet rs = st.executeQuery(sentSQL);
			while(rs.next()) {//mientras el rs tenga elementos los almacenamos 
				//creamos una nueva bebida vacia y le colocamos los valores correspondientes
				Bebida b = new Bebida();
				b.id = rs.getInt("cod");
				b.nombre = rs.getString("nombre");
				b.precio = rs.getDouble("precio");
				b.alcoholica = rs.getBoolean("alcoholica");
				bebidas.add(b);
				}
			
			//cargamos las comidas
			sentSQL = "select * from comida";
			rs = st.executeQuery(sentSQL);
			while(rs.next()) {
				Comida c = new Comida();
				c.id = rs.getInt("cod");
				c.nombre = rs.getString("nombre");
				c.precio = rs.getDouble("precio");
				c.numeroPlato = rs.getInt("numeroPlato");
				c.tipoImagen = rs.getInt("imagen");
				comidas.add(c);
			}

		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		
	}

	
	//escribir fichero de texto
//		//metodo para escribir en el fichero de texto los cambios en la carta
//		FileWriter archivo = null;
//		PrintWriter pw = null;
//
//		try {
//			archivo = new FileWriter("src/ficherosDeTexto/carta.txt");
//			pw = new PrintWriter(archivo);
//			for (Bebida bebida : bebidas) {
//				pw.println(bebida.toString());
//			}
//			for (Comida comida : comidas) {
//				pw.println(comida.toString());
//			}
//			 JOptionPane.showMessageDialog(null,"se ha escrito correctamente" );
//		
//		} catch (Exception e) {
//			e.printStackTrace();
//		//	System.err.println("no encontrado");
//			 JOptionPane.showMessageDialog(null,"no encontrado","error",0 );
//		}finally {
//			pw.close();
//			try {
//				archivo.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				//System.err.println("no se cerr�");
//				 JOptionPane.showMessageDialog(null,"no se cerr�","error",0 );
//			}
//		}
	
	
	
	public static void main(String[] args) {
		/*Carta c = new Carta();
		c.cargarCarta();
		c.guardarCarta();
		 */
	}

}
