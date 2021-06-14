package itdelatrisu.tripletriad;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

public class SelectedCardsFrame extends JFrame{

	private static final long serialVersionUID = 1391482831207151808L;
	private JPanel contentPane;
	private JTextField fieldCodeCard;
	private Vector<Card> cardsSelected;
	private DefaultListModel<Card> cardModel;
	private JList cardListSelected;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SelectedCardsFrame frame = new SelectedCardsFrame();
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
	public SelectedCardsFrame() {
		this.cardsSelected = new Vector<Card>();
		setTitle("Select Yours Cards");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 483, 325);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		panel.setToolTipText("");
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		createLabelCards(panel);
		createFieldCodeCard(panel);
		createButtonAdd(panel, createComboBoxCards(panel));
		createButtonPlay(panel);
		createCardList(panel);
		createButtonRandom(panel);
		createButtonRemove(panel);

	}

	private void createButtonRemove(JPanel panel) {
		JButton btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Card selected = (Card) cardListSelected.getSelectedValue();
				cardModel.removeElement(selected);
			}
		});
		btnRemove.setBounds(319, 183, 117, 25);
		panel.add(btnRemove);
	}

	private void createButtonRandom(JPanel panel) {
		JButton btnRandom = new JButton("Random");
		btnRandom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Vector<Card> cards = new Vector<Card>();
				cards.addAll(getCards());
				Collections.shuffle(cards);
				if (!cardModel.isEmpty()) {
					cardModel.removeAllElements();
				}
				for (int i = 0; i <= 4; i++) {
					cardModel.addElement(cards.get(i));
				}
			}
		});
		btnRandom.setBounds(319, 67, 91, 25);
		panel.add(btnRandom);
	}

	private void createCardList(JPanel panel) {
		cardModel = new DefaultListModel<Card>();
		cardListSelected = new JList(cardModel);
		cardListSelected.setVisibleRowCount(5);
		cardListSelected.setForeground(Color.BLUE);
		cardListSelected.setBackground(Color.WHITE);
		cardListSelected.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		cardListSelected.setCellRenderer(new cellRenderListCardSelected());
		cardListSelected.setBounds(66, 119, 241, 102);
		panel.add(cardListSelected);
	}

	private void createButtonPlay(JPanel panel) {
		JButton btnPlay = new JButton("Play");
		btnPlay.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				ArrayList<Card> cards = new ArrayList<Card>();
				ListModel model = cardListSelected.getModel();
				for (int i = 0; i < model.getSize(); i++) {
					Card item = (Card) model.getElementAt(i);
					cards.add(item);
				}
				if (cards.size() == 5) {
					TripleTriad.initGame(cards);
				} else {
					JOptionPane.showMessageDialog(SelectedCardsFrame.this, "PLEASE SELECT JUST 5 CARDS", "",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnPlay.setBounds(319, 253, 117, 25);
		panel.add(btnPlay);
	}

	private void createButtonAdd(JPanel panel, JComboBox<Card> comboCard) {
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Card card = (Card) comboCard.getSelectedItem();
				String cardCodeText = fieldCodeCard.getText();

				if (cardCodeText != null && !cardCodeText.isEmpty()) {
					Card cardFinded = getCard(cardCodeText);
					if (cardFinded != null) {
						cardModel.addElement(cardFinded);
						fieldCodeCard.setText(null);
						return;
					} else {
						JOptionPane.showMessageDialog(SelectedCardsFrame.this, "Card Invalid", "",
								JOptionPane.ERROR_MESSAGE);
					}
				}

				if (card == null) {
					JOptionPane.showMessageDialog(SelectedCardsFrame.this, "Please Select Valid Card", "Triple Triad",
							JOptionPane.ERROR_MESSAGE);
				} else {
					cardModel.addElement(card);
					comboCard.setSelectedIndex(-1);
				}
			}
		});
		btnAdd.setBounds(319, 33, 63, 25);
		panel.add(btnAdd);
	}

	private void createFieldCodeCard(JPanel panel) {
		fieldCodeCard = new JTextField();
		fieldCodeCard.setToolTipText("You can add cards by their number");
		fieldCodeCard.setBounds(66, 70, 241, 19);
		panel.add(fieldCodeCard);
		fieldCodeCard.setColumns(10);
	}

	private JComboBox<Card> createComboBoxCards(JPanel panel) {
		Vector<Card> cards = new Vector<Card>();
		cards.add(null);
		cards.addAll(getCards());
		JComboBox<Card> comboCard = new JComboBox(cards);
		comboCard.setRenderer(new cellRenderComboCard());
		comboCard.setBounds(66, 33, 241, 24);
		panel.add(comboCard);
		return comboCard;
	}

	private void createLabelCards(JPanel panel) {
		JLabel lblCartas = new JLabel("Cartas");
		lblCartas.setBounds(12, 56, 70, 15);
		panel.add(lblCartas);
	}

	private Vector<Card> getCards() {
		Vector<Card> list = new Vector<Card>();
		Deck deck = new Deck();
		list.addAll(deck.getDeck());
		return list;
	}

	public class cellRenderListCardSelected extends DefaultListCellRenderer {

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			Card c = (Card) value;
			StringBuilder sb = new StringBuilder();
			sb.append(c.getID());
			sb.append(" - ");
			sb.append(c.getName());
			sb.append(" - ");
			sb.append(c.getElement());
			return new JLabel(sb.toString());
		}

	}

	public class cellRenderComboCard extends DefaultListCellRenderer {

		private static final long serialVersionUID = 9169251708096343445L;

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			Card c = (Card) value;
			if (value == null) {
				return new JLabel("-");
			} else {
				StringBuilder sb = new StringBuilder();
				sb.append(c.getID());
				sb.append(" - ");
				sb.append(c.getName());
				sb.append(" - ");
				sb.append(c.getElement());
				return new JLabel(sb.toString());
			}
		}
	}

	public Card getCard(String code) {
		int codeIntValue = Integer.valueOf(code).intValue();
		Vector<Card> cards = getCards();
		for (Card card : cards) {
			if (card.getID() == codeIntValue) {
				return card;
			}
		}
		return null;
	}
}
