package itdelatrisu.tripletriad;

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class SelectCardsFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SelectCardsFrame frame = new SelectCardsFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SelectCardsFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 440, 391);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		textField = new JTextField();
		contentPane.add(textField, "6, 4, fill, default");
		textField.setColumns(10);
		
		JLabel lblCarta = new JLabel("Carta 1");
		contentPane.add(lblCarta, "4, 6, left, default");
		
		textField_1 = new JTextField();
		contentPane.add(textField_1, "6, 8, fill, default");
		textField_1.setColumns(10);
		
		JLabel lblCarta_1 = new JLabel("Carta 2");
		contentPane.add(lblCarta_1, "4, 10");
		
		textField_2 = new JTextField();
		contentPane.add(textField_2, "6, 12, fill, default");
		textField_2.setColumns(10);
		
		JLabel lblCarta_2 = new JLabel("Carta 3");
		contentPane.add(lblCarta_2, "4, 14");
		
		textField_3 = new JTextField();
		contentPane.add(textField_3, "6, 16, fill, default");
		textField_3.setColumns(10);
		
		JLabel lblCarta_3 = new JLabel("Carta 4");
		contentPane.add(lblCarta_3, "4, 18");
		
		textField_4 = new JTextField();
		contentPane.add(textField_4, "6, 20, fill, default");
		textField_4.setColumns(10);
		
		JLabel lblCarta_4 = new JLabel("Carta 5");
		contentPane.add(lblCarta_4, "4, 22");
		
		JButton btnIniciarJogo = new JButton("iniciar Jogo");
		btnIniciarJogo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Deck deck = new Deck();
				
				ArrayList<Card> cartas = deck.getDeck();
				
				Card card1 = cartas.get(Integer.parseInt(textField.getText())-1);
				Card card2 = cartas.get(Integer.parseInt(textField_1.getText())-1);
				Card card3 = cartas.get(Integer.parseInt(textField_2.getText())-1);
				Card card4 = cartas.get(Integer.parseInt(textField_3.getText())-1);
				Card card5 = cartas.get(Integer.parseInt(textField_4.getText())-1);
				
				ArrayList<Card> cartasJogaveis = new ArrayList<Card>();
				cartasJogaveis.add(card1);
				cartasJogaveis.add(card2);
				cartasJogaveis.add(card3);
				cartasJogaveis.add(card4);
				cartasJogaveis.add(card5);				
				TripleTriad.initGame(cartasJogaveis);
			}
		});
		contentPane.add(btnIniciarJogo, "6, 26");
	}

}
