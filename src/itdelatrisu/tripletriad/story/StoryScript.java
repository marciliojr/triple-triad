/*
 * Triple Triad - a card game from FFVIII
 * Copyright (C) 2014 Jeffrey Han
 *
 * Triple Triad is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package itdelatrisu.tripletriad.story;

import java.util.ArrayList;

import itdelatrisu.tripletriad.Options;
import itdelatrisu.tripletriad.Rule;
import itdelatrisu.tripletriad.story.StoryCampaign.Duel;
import itdelatrisu.tripletriad.story.StoryCampaign.Kind;
import itdelatrisu.tripletriad.story.StoryCampaign.Line;
import itdelatrisu.tripletriad.story.StoryCampaign.Region;
import itdelatrisu.tripletriad.story.StoryCampaign.Ruleset;

/**
 * World Triple Triad Tournament script (PT, EN, ES).
 * Nilo Vann calls the tables. Squall explains a rule the first time it appears.
 * Opponents only talk at the table. Official FFVIII names are kept.
 */
public final class StoryScript {
	private StoryScript() {}

	static Line[] starter() {
		return new Line[] {
			N("Aqui é Nilo Vann, locutor do Torneio Mundial de Triple Triad. {name} entra com um deck próprio da campanha, separado da coleção do perfil.",
				"This is Nilo Vann, voice of the World Triple Triad Tournament. {name} enters with a campaign deck of their own, kept apart from the profile album.",
				"Aquí Nilo Vann, locutor del Torneo Mundial de Triple Triad. {name} entra con un mazo propio de la campaña, separado de la colección del perfil."),
			Q("{name}. Você monta a mão de 5 cartas nessa coleção. Cartas do perfil não entram, e o que você ganhar fica só na campanha.",
				"{name}. You build the 5-card hand from that collection. Profile cards stay out, and what you win stays in the campaign.",
				"{name}. Montas la mano de 5 cartas en esa colección. Las cartas del perfil no entran, y lo que ganes se queda solo en la campaña."),
			Q("A troca desta campanha não é One, Diff, Direct nem All. Se vencer, escolhe 1 carta do deck adversário. No chefe da cidade, leva também uma carta fixa. Derrota não tira nada.",
				"This campaign's trade is not One, Diff, Direct or All. Win, and you pick 1 card from the opponent's deck. Beat a city boss, and you also get a fixed card. A loss takes nothing.",
				"El intercambio de esta campaña no es One, Diff, Direct ni All. Si ganas, eliges 1 carta del mazo rival. En el jefe de la ciudad, también llevas una carta fija. La derrota no quita nada."),
			Q("Random embaralharia as duas mãos. Aqui você monta a sua e ela permanece. A primeira mesa é Open: os ranks dela ficam à vista. Depois, Same e Combo.",
				"Random would shuffle both hands. Here you build yours and it stays. The first table is Open: you can see her ranks. After that, Same and Combo.",
				"Random barajaria las dos manos. Aquí montas la tuya y se queda. La primera mesa es Open: ves sus rangos. Después, Same y Combo."),
			N("A seletiva do Garden é uma chave contínua. Quem vence enfrenta o próximo. Quem perde repete o confronto e não é eliminado. {name}, a primeira cadeira é a de Lina Faris.",
				"The Garden qualifier is a ladder. A win faces the next seed. A loss replays the same tie and does not eliminate you. {name}, the first chair belongs to Lina Faris.",
				"La selectiva del Garden es una llave continua. Quien gana enfrenta al siguiente. Quien pierde repite el cruce y no queda eliminado. {name}, la primera silla es la de Lina Faris.")
		};
	}

	static Line[] ceremony() {
		return new Line[] {
			N("Fim de jogo no Garden! {name} vence a grande final e leva o título de {campeão|campeã} mundial de Triple Triad!",
				"Game over at the Garden! {name} wins the grand final and takes the world Triple Triad title!",
				"¡Fin del juego en el Garden! ¡{name} gana la gran final y se lleva el título de {campeón|campeona} mundial de Triple Triad!"),
			Q("Você fechou a última mesa. O título é seu. Whatever.",
				"You closed the last table. The title is yours. Whatever.",
				"Cerraste la última mesa. El título es tuyo. Whatever.")
		};
	}

	/**
	 * Squall lines for rules not yet seen in this campaign, plus the trade note once.
	 * Marks them as seen so a rematch does not repeat the lesson.
	 */
	public static Line[] lessons(StorySave save, Duel duel) {
		ArrayList<Line> out = new ArrayList<Line>();
		if (save == null || duel == null || duel.rules == null)
			return new Line[0];
		if (!save.hasSeenRule("TRADE")) {
			out.add(lessonTrade());
			out.add(lessonTradeClassic());
			save.markRuleSeen("TRADE");
		}
		if (!save.hasSeenRule("RANDOM")) {
			out.add(lessonRandom());
			save.markRuleSeen("RANDOM");
		}
		for (int i = 0; i < duel.rules.on.length; i++) {
			Rule rule = duel.rules.on[i];
			String key = rule.name();
			if (save.hasSeenRule(key))
				continue;
			Line line = lesson(rule);
			if (line != null) {
				out.add(line);
				save.markRuleSeen(key);
			}
		}
		return out.toArray(new Line[0]);
	}

	private static Line lesson(Rule rule) {
		switch (rule) {
			case OPEN: return Q(
				"Open: a mão dela fica à vista. Você lê os ranks e ataca o lado fraco, sem blefe. Neste exemplo a Geezard (direita 4) entra à esquerda da Red Bat (esquerda 2). 4 é maior que 2, e a Red Bat vira.",
				"Open: her hand stays in sight. You read the ranks and attack the weak side. There is no bluff. In this example Geezard (right 4) lands left of Red Bat (left 2). 4 is higher than 2, and Red Bat flips.",
				"Open: su mano queda a la vista. Lees los rangos y atacas el lado débil, sin farol. En este ejemplo la Geezard (derecha 4) entra a la izquierda de la Red Bat (izquierda 2). 4 es mayor que 2, y la Red Bat gira.",
				StoryRuleExample.OPEN);
			case SAME: return Q(
				"Same: se dois lados da carta que você joga empatam com os vizinhos no mesmo número, os dois viram, mesmo que o rank não seja maior. Neste exemplo a Thrustaevis no centro fecha 5 com a Grand Mantis em cima e 5 com a Fastitocalon-F à esquerda.",
				"Same: if two sides of the card you play tie the neighbors on the same number, both flip, even if your rank is not higher. In this example Thrustaevis in the center ties 5 with Grand Mantis above and 5 with Fastitocalon-F on the left.",
				"Same: si dos lados de la carta que juegas empatan con los vecinos en el mismo número, las dos giran, aunque tu rango no sea mayor. En este ejemplo la Thrustaevis en el centro cierra 5 con la Grand Mantis arriba y 5 con la Fastitocalon-F a la izquierda.",
				StoryRuleExample.SAME);
			case SAME_WALL: return Q(
				"Same Wall: a borda da mesa conta como A (10). Se a carta que você joga tem um A na parede, basta um outro lado empatar com o vizinho para fechar o Same. Neste exemplo a PuPu no canto tem A na borda direita e 1 igual à Funguar. A Funguar vira.",
				"Same Wall: the edge of the table counts as A (10). If the card you play has an A on the wall, one other side that ties a neighbor is enough to close Same. In this example PuPu in the corner has A on the right edge and a 1 that matches Funguar. Funguar flips.",
				"Same Wall: el borde cuenta como A (10). Si tu carta tiene un A en la pared, basta un lado que empate con el vecino para cerrar el Same. En este ejemplo la PuPu en la esquina tiene A a la derecha y un 1 igual al de la Funguar. La Funguar gira.",
				StoryRuleExample.SAME_WALL);
			case PLUS: return Q(
				"Plus: some o seu lado com o da vizinha. Se duas somas forem iguais, as duas viram. Neste exemplo a Vysage no centro faz 6+2 com a Fastitocalon-F em cima e 5+3 com a Bite Bug à esquerda. As somas dão 8, e as duas viram.",
				"Plus: add your side to the neighbor's. If two sums match, both flip. In this example Vysage in the center makes 6+2 with Fastitocalon-F above and 5+3 with Bite Bug on the left. Both sums are 8, and both cards flip.",
				"Plus: suma tu lado con el de la vecina. Si dos sumas son iguales, las dos giran. En este ejemplo la Vysage en el centro hace 6+2 con la Fastitocalon-F arriba y 5+3 con la Bite Bug a la izquierda. Las dos sumas dan 8, y las dos giran.",
				StoryRuleExample.PLUS);
			case COMBO: return Q(
				"Combo: depois de um Same ou Plus, as cartas que acabaram de virar atacam de novo, como se você as tivesse jogado. Neste exemplo o Same vira a Grand Mantis, e o 2 da direita dela é maior que o 1 da Grat. A Grat também vira.",
				"Combo: after a Same or Plus, the cards that just flipped attack again, as if you had played them. In this example Same flips Grand Mantis, and its right 2 is higher than Grat's left 1. Grat flips too.",
				"Combo: después de un Same o Plus, las cartas que acaba de girar atacan de nuevo, como si tú las hubieras jugado. En este ejemplo el Same gira a la Grand Mantis, y el 2 de su derecha es mayor que el 1 de la Grat. La Grat también gira.",
				StoryRuleExample.COMBO);
			case ELEMENTAL: return Q(
				"Elemental: a casa tem um elemento. Carta do mesmo recebe +1 naquele lado; de outro, -1. Same e Plus usam o rank puro; só a captura comum usa o bônus. Neste exemplo a SAM08G de Fogo entra numa casa de Fogo: o 6 vira 7 e supera o 6 da Armadodo.",
				"Elemental: the square has an element. A matching card gets +1 on that side; a different one, -1. Same and Plus use the raw rank; only a normal capture uses the bonus. In this example Fire SAM08G lands on a Fire square: the 6 becomes 7 and beats Armadodo's 6.",
				"Elemental: la casilla tiene un elemento. Carta del mismo: +1 en ese lado; otra, -1. Same y Plus usan el rango puro; solo la captura común usa el bono. En este ejemplo la SAM08G de Fuego entra en fuego: el 6 pasa a 7 y supera el 6 de la Armadodo.",
				StoryRuleExample.ELEMENTAL);
			case SUDDEN_DEATH: return Q(
				"Sudden Death: se a mesa fecha 5 a 5, o jogo não acaba. Começa outra mesa com as mesmas cartas, cada uma na cor de quem a controlava no empate. Não trate o 5 a 5 como o fim.",
				"Sudden Death: if the table closes 5 to 5, the game is not over. Another table starts with the same cards, each in the color of who controlled it at the tie. Don't treat 5 to 5 as the end.",
				"Sudden Death: si la mesa cierra 5 a 5, el juego no termina. Empieza otra mesa con las mismas cartas, cada una en el color de quien la controlaba en el empate. No tomes el 5 a 5 como el final.");
			default: return null;
		}
	}

	private static Line lessonTrade() {
		return Q(
			"A troca desta campanha não é One, Diff, Direct nem All. Se você vencer, escolhe 1 carta do deck que acabou de enfrentar, como a carta marcada neste exemplo. No chefe da cidade, leva também uma carta fixa. Derrota não tira nada.",
			"This campaign's trade is not One, Diff, Direct or All. If you win, you pick 1 card from the deck you just faced, like the marked card in this example. Beat a city boss, and you also get a fixed card. A loss takes nothing.",
			"El intercambio de esta campaña no es One, Diff, Direct ni All. Si ganas, eliges 1 carta del mazo que acabas de enfrentar, como la carta marcada en este ejemplo. En el jefe de la ciudad, también llevas una carta fija. La derrota no quita nada.",
			StoryRuleExample.TRADE);
	}

	private static Line lessonTradeClassic() {
		return Q(
			"No jogo clássico, One troca só 1 carta; Diff troca a diferença do placar; Direct: cada um fica com as cartas que virou; All: o vencedor leva o deck inteiro. Nenhuma dessas regras vale nesta campanha.",
			"In the classic game, One trades only 1 card; Diff trades the score difference; Direct: each player keeps the cards they flipped; All: the winner takes the whole deck. None of those rules apply in this campaign.",
			"En el juego clásico, One intercambia solo 1 carta; Diff, la diferencia del marcador; Direct: cada uno se queda con las cartas que giró; All: el ganador se lleva el mazo entero. Ninguna de esas reglas vale en esta campaña.");
	}

	private static Line lessonRandom() {
		return Q(
			"Random embaralharia as duas mãos, e você perderia a mão de 5 que acabou de montar. Nesta campanha a regra não é usada: a mão que você escolhe é a que entra na mesa.",
			"Random would shuffle both hands, and you would lose the 5-card hand you just built. This campaign does not use that rule: the hand you pick is the one that enters the table.",
			"Random barajaria las dos manos, y perderías la mano de 5 que acabas de montar. En esta campaña no se usa esa regla: la mano que eliges es la que entra en la mesa.");
	}

	static Region[] build() {
		return new Region[] { garden(), town(), timber(), deling(), dollet(), fh(), esthar(), fin() };
	}

	private static Region garden() {
		return region("garden", "intro_historia", "mesa_balamb_garden.png", Kind.LADDER, 0, null,
			"Garden - Seletiva", "Garden - Qualifier", "Garden - Selectiva",
			new Line[] {
				N("Aqui é Nilo Vann, locutor do Torneio Mundial de Triple Triad! As inscrições abrem em Balamb Garden.",
					"This is Nilo Vann, voice of the World Triple Triad Tournament! Registration opens at Balamb Garden.",
					"¡Aquí Nilo Vann, locutor del Torneo Mundial de Triple Triad! Las inscripciones abren en Balamb Garden."),
				Q("A seletiva é uma chave. Quem vence enfrenta o próximo. Quem perde repete a mesa. Primeira regra: Open.",
					"The qualifier is a ladder. A win faces the next seed. A loss replays the table. First rule: Open.",
					"La selectiva es una llave. Quien gana enfrenta al siguiente. Quien pierde repite la mesa. Primera regla: Open."),
				N("Quem vencer a final da seletiva entra no mundial. {name}, a primeira cadeira é a de Lina Faris.",
					"Whoever wins the qualifier final enters the world tournament. {name}, the first chair belongs to Lina Faris.",
					"Quien gane la final de la selectiva entra al mundial. {name}, la primera silla es la de Lina Faris.")
			},
			new Duel[] {
				duel("cadete", "npc_cadete", "Lina Faris", "Lina Faris", "Lina Faris", 1, 2,
					Options.AIType.DEFENSIVE, Ruleset.OPEN_BASIC, 0, false,
					"Cadete de Balamb", "Balamb cadet", "Cadete de Balamb",
					"Mão aberta, jogo curto e defensivo.", "Open hand, short defensive game.", "Mano abierta, partida corta y defensiva.",
					new Line[] {
					N("Lina Faris, cadete de Balamb Garden. Primeira mesa da seletiva.",
						"Lina Faris, a Balamb Garden cadet. First table of the qualifier.",
						"Lina Faris, cadete de Balamb Garden. Primera mesa de la selectiva."),
					N("Regras: Open, Same e Combo. A recompensa é 1 carta do deck dela se {name} vencer.",
						"Rules: Open, Same and Combo. The prize is 1 card from her deck if {name} wins.",
						"Reglas: Open, Same y Combo. La recompensa es 1 carta de su mazo si {name} gana.")
					},
					new Line[] {
					N("Seletiva do Garden, mesa 1 de 6! Lina Faris, cadete de Balamb, contra {name}!",
						"Garden qualifier, table 1 of 6! Lina Faris, Balamb cadet, against {name}!",
						"¡Selectiva del Garden, mesa 1 de 6! ¡Lina Faris, cadete de Balamb, contra {name}!"),
					N("Regras da mesa: Open, Same e Combo. Em jogo: 1 carta do deck de Lina. Sem carta de chefe nesta cadeira.",
						"Table rules: Open, Same and Combo. At stake: 1 card from Lina's deck. No boss card on this chair.",
						"Reglas de la mesa: Open, Same y Combo. En juego: 1 carta del mazo de Lina. Sin carta de jefe en esta silla.")
					},
					say("npc_cadete", "Lina Faris", "Lina Faris", "Lina Faris",
						"Vou jogar com tudo à vista. Leia a minha mão e tente virar um lado.",
						"I'll play with everything in sight. Read my hand and try to flip a side.",
						"Voy a jugar con todo a la vista. Lee mi mano e intenta girar un lado."),
					lost("npc_cadete", "Lina Faris", "Lina Faris", "Lina Faris",
						"Boa leitura. Você viu os ranks e fechou a mesa. Levo esta.",
						"Good reading. You saw the ranks and closed the table. I'll take this one.",
						"Buena lectura. Viste los rangos y cerraste la mesa. Me llevo esta."),
					say("npc_cadete", "Lina Faris", "Lina Faris", "Lina Faris",
						"A mão estava aberta e mesmo assim eu fechei. Tente de novo quando quiser.",
						"The hand was open and I still closed it. Try again when you like.",
						"La mano estaba abierta y aun así la cerré. Inténtalo de nuevo cuando quieras."),
					new Line[] {
					N("Vitória de {name}! Escolha 1 carta do deck que você acabou de enfrentar. A derrota não teria tirado nada.",
						"Win for {name}! Choose 1 card from the deck you just faced. A loss would have taken nothing.",
						"¡Victoria de {name}! Elige 1 carta del mazo que acabas de enfrentar. La derrota no habría quitado nada.")
					},
					new Line[] {
					N("Derrota de {name}. Reconhecimento esportivo: a mesa fica aberta. Pode tentar de novo, sem perder carta.",
						"Defeat for {name}. A sportsmanlike reading: the table stays open. You may try again, and you lose no card.",
						"Derrota de {name}. Reconocimiento deportivo: la mesa sigue abierta. Puedes intentarlo de nuevo, sin perder carta.")
					}),
				duel("spade", "cc_spade", "Rik Calden", "Rik Calden", "Rik Calden", 1, 2,
					Options.AIType.DEFENSIVE, Ruleset.BASIC, 0, false,
					"Cadete de Balamb", "Balamb cadet", "Cadete de Balamb",
					"Same e Combo, sem Open. Ele esconde os ranks.", "Same and Combo, no Open. He hides his ranks.", "Same y Combo, sin Open. Él esconde los rangos.",
					new Line[] {
					N("Rik Calden, cadete de Balamb. Segunda mesa da seletiva.",
						"Rik Calden, a Balamb cadet. Second table of the qualifier.",
						"Rik Calden, cadete de Balamb. Segunda mesa de la selectiva."),
					N("Open sai da mesa. Same e Combo continuam. Recompensa: 1 carta do deck dele.",
						"Open leaves the table. Same and Combo stay. Prize: 1 card from his deck.",
						"Open sale de la mesa. Same y Combo siguen. Recompensa: 1 carta de su mazo.")
					},
					new Line[] {
					N("Seletiva do Garden, mesa 2 de 6! Rik Calden, cadete de Balamb, contra {name}!",
						"Garden qualifier, table 2 of 6! Rik Calden, Balamb cadet, against {name}!",
						"¡Selectiva del Garden, mesa 2 de 6! ¡Rik Calden, cadete de Balamb, contra {name}!"),
					N("Regras: Same e Combo. Sem Open: os ranks dele ficam ocultos. Em jogo: 1 carta do deck de Rik.",
						"Rules: Same and Combo. No Open: his ranks stay hidden. At stake: 1 card from Rik's deck.",
						"Reglas: Same y Combo. Sin Open: sus rangos quedan ocultos. En juego: 1 carta del mazo de Rik.")
					},
					say("cc_spade", "Rik Calden", "Rik Calden", "Rik Calden",
						"Agora você não vê a minha mão. Ache o Same no escuro.",
						"Now you don't see my hand. Find the Same in the dark.",
						"Ahora no ves mi mano. Encuentra el Same a oscuras."),
					lost("cc_spade", "Rik Calden", "Rik Calden", "Rik Calden",
						"Você fechou o Same sem ver os ranks. Isso vale a vaga.",
						"You closed the Same without seeing the ranks. That earns the spot.",
						"Cerraste el Same sin ver los rangos. Eso vale la plaza."),
					say("cc_spade", "Rik Calden", "Rik Calden", "Rik Calden",
						"Sem Open, você chutou o lado errado. Eu fico com a mesa.",
						"With no Open, you guessed the wrong side. The table stays mine.",
						"Sin Open, apostaste al lado equivocado. La mesa se queda conmigo."),
					new Line[] {
					N("Vitória de {name}! Escolha 1 carta do deck que você acabou de enfrentar. A derrota não teria tirado nada.",
						"Win for {name}! Choose 1 card from the deck you just faced. A loss would have taken nothing.",
						"¡Victoria de {name}! Elige 1 carta del mazo que acabas de enfrentar. La derrota no habría quitado nada.")
					},
					new Line[] {
					N("Derrota de {name}. Reconhecimento esportivo: a mesa fica aberta. Pode tentar de novo, sem perder carta.",
						"Defeat for {name}. A sportsmanlike reading: the table stays open. You may try again, and you lose no card.",
						"Derrota de {name}. Reconocimiento deportivo: la mesa sigue abierta. Puedes intentarlo de nuevo, sin perder carta.")
					}),
				duel("joker", "cc_joker", "Toma Breck", "Toma Breck", "Toma Breck", 1, 3,
					Options.AIType.DEFENSIVE, Ruleset.BASIC, 0, false,
					"Cadete de Balamb", "Balamb cadet", "Cadete de Balamb",
					"Mesma regra, um degrau acima. Ele espera o Combo.", "Same rules, one step up. He waits for the Combo.", "Misma regla, un peldaño más. Él espera el Combo.",
					new Line[] {
					N("Toma Breck, cadete de Balamb. Terceira mesa da seletiva.",
						"Toma Breck, a Balamb cadet. Third table of the qualifier.",
						"Toma Breck, cadete de Balamb. Tercera mesa de la selectiva."),
					N("Same e Combo, cartas um pouco mais altas. Recompensa: 1 carta do deck dele.",
						"Same and Combo, slightly higher cards. Prize: 1 card from his deck.",
						"Same y Combo, cartas un poco más altas. Recompensa: 1 carta de su mazo.")
					},
					new Line[] {
					N("Seletiva do Garden, mesa 3 de 6! Toma Breck, cadete de Balamb, contra {name}!",
						"Garden qualifier, table 3 of 6! Toma Breck, Balamb cadet, against {name}!",
						"¡Selectiva del Garden, mesa 3 de 6! ¡Toma Breck, cadete de Balamb, contra {name}!"),
					N("Regras: Same e Combo. Em jogo: 1 carta do deck de Toma. A chave segue se {name} vencer.",
						"Rules: Same and Combo. At stake: 1 card from Toma's deck. The ladder moves if {name} wins.",
						"Reglas: Same y Combo. En juego: 1 carta del mazo de Toma. La llave sigue si {name} gana.")
					},
					say("cc_joker", "Toma Breck", "Toma Breck", "Toma Breck",
						"Se você soltar um Combo cedo demais, eu fecho o resto. Tenta.",
						"If you drop a Combo too early, I'll close the rest. Try me.",
						"Si sueltas un Combo demasiado pronto, cierro el resto. Inténtalo."),
					lost("cc_joker", "Toma Breck", "Toma Breck", "Toma Breck",
						"O Combo ficou com você até o fim. Levo o recado.",
						"You held the Combo until the end. I'll take the note.",
						"El Combo se quedó contigo hasta el final. Me llevo el recado."),
					say("cc_joker", "Toma Breck", "Toma Breck", "Toma Breck",
						"Você abriu o Combo cedo e eu tomei o tabuleiro. Outra vez?",
						"You opened the Combo early and I took the board. Again?",
						"Abriste el Combo pronto y me llevé el tablero. ¿Otra vez?"),
					new Line[] {
					N("Vitória de {name}! Escolha 1 carta do deck que você acabou de enfrentar. A derrota não teria tirado nada.",
						"Win for {name}! Choose 1 card from the deck you just faced. A loss would have taken nothing.",
						"¡Victoria de {name}! Elige 1 carta del mazo que acabas de enfrentar. La derrota no habría quitado nada.")
					},
					new Line[] {
					N("Derrota de {name}. Reconhecimento esportivo: a mesa fica aberta. Pode tentar de novo, sem perder carta.",
						"Defeat for {name}. A sportsmanlike reading: the table stays open. You may try again, and you lose no card.",
						"Derrota de {name}. Reconocimiento deportivo: la mesa sigue abierta. Puedes intentarlo de nuevo, sin perder carta.")
					}),
				duel("club", "cc_club", "Nira Voss", "Nira Voss", "Nira Voss", 2, 3,
					Options.AIType.BALANCED, Ruleset.PLUS, 0, false,
					"Cadete veterana de Balamb", "Senior Balamb cadet", "Cadete veterana de Balamb",
					"Plus entra pela primeira vez, junto de Same e Combo.", "Plus arrives for the first time, with Same and Combo.", "Plus entra por primera vez, junto a Same y Combo.",
					new Line[] {
					N("Nira Voss, cadete veterana de Balamb Garden. Quarta mesa da seletiva.",
						"Nira Voss, a senior Balamb Garden cadet. Fourth table of the qualifier.",
						"Nira Voss, cadete veterana de Balamb Garden. Cuarta mesa de la selectiva."),
					N("Aqui entra Plus. Some dois lados iguais e as cartas viram. Recompensa: 1 carta do deck dela.",
						"Plus arrives here. Equal sums on two sides flip the cards. Prize: 1 card from her deck.",
						"Aquí entra Plus. Suma dos lados iguales y las cartas giran. Recompensa: 1 carta de su mazo.")
					},
					new Line[] {
					N("Seletiva do Garden, mesa 4 de 6! Nira Voss, cadete veterana de Balamb, contra {name}!",
						"Garden qualifier, table 4 of 6! Nira Voss, senior Balamb cadet, against {name}!",
						"¡Selectiva del Garden, mesa 4 de 6! ¡Nira Voss, cadete veterana de Balamb, contra {name}!"),
					N("Regras: Same, Plus e Combo. Em jogo: 1 carta do deck de Nira.",
						"Rules: Same, Plus and Combo. At stake: 1 card from Nira's deck.",
						"Reglas: Same, Plus y Combo. En juego: 1 carta del mazo de Nira.")
					},
					say("cc_club", "Nira Voss", "Nira Voss", "Nira Voss",
						"Plus não é chute. Some antes de soltar a carta.",
						"Plus is not a guess. Add it up before you drop the card.",
						"Plus no es un palo. Suma antes de soltar la carta."),
					lost("cc_club", "Nira Voss", "Nira Voss", "Nira Voss",
						"Você somou certo e segurou o Combo. A vaga anda.",
						"You added it right and held the Combo. The spot moves.",
						"Sumaste bien y aguantaste el Combo. La plaza avanza."),
					say("cc_club", "Nira Voss", "Nira Voss", "Nira Voss",
						"A soma não fechou. Eu vi o Plus e levei as duas. De novo, se quiser.",
						"The sum didn't close. I saw the Plus and took both. Again, if you want.",
						"La suma no cerró. Vi el Plus y me llevé las dos. Otra vez, si quieres."),
					new Line[] {
					N("Vitória de {name}! Escolha 1 carta do deck que você acabou de enfrentar. A derrota não teria tirado nada.",
						"Win for {name}! Choose 1 card from the deck you just faced. A loss would have taken nothing.",
						"¡Victoria de {name}! Elige 1 carta del mazo que acabas de enfrentar. La derrota no habría quitado nada."),
					Q("Plus exige duas somas iguais. Não solte um 6 se você não tem o par.",
						"Plus needs two equal sums. Don't drop a 6 if you don't have the pair.",
						"Plus exige dos sumas iguales. No sueltes un 6 si no tienes el par.")
					},
					new Line[] {
					N("Derrota de {name}. Reconhecimento esportivo: a mesa fica aberta. Pode tentar de novo, sem perder carta.",
						"Defeat for {name}. A sportsmanlike reading: the table stays open. You may try again, and you lose no card.",
						"Derrota de {name}. Reconocimiento deportivo: la mesa sigue abierta. Puedes intentarlo de nuevo, sin perder carta.")
					}),
				duel("heart", "cc_heart", "Xu", "Xu", "Xu", 3, 4,
					Options.AIType.BALANCED, Ruleset.PLUS, 0, false,
					"Instrutora do Garden", "Garden instructor", "Instructora del Garden",
					"Plus firme, cartas de nível médio. Pouca pressa.", "Steady Plus, mid-level cards. She is in no hurry.", "Plus firme, cartas de nivel medio. Poca prisa.",
					new Line[] {
					N("Xu, instrutora de Balamb Garden. Quinta mesa da seletiva.",
						"Xu, a Balamb Garden instructor. Fifth table of the qualifier.",
						"Xu, instructora de Balamb Garden. Quinta mesa de la selectiva."),
					N("Same, Plus e Combo. Recompensa: 1 carta do deck dela. Depois vem a final da seletiva.",
						"Same, Plus and Combo. Prize: 1 card from her deck. The qualifier final is next.",
						"Same, Plus y Combo. Recompensa: 1 carta de su mazo. Después llega la final de la selectiva.")
					},
					new Line[] {
					N("Seletiva do Garden, mesa 5 de 6! Xu, instrutora do Garden, contra {name}!",
						"Garden qualifier, table 5 of 6! Xu, Garden instructor, against {name}!",
						"¡Selectiva del Garden, mesa 5 de 6! ¡Xu, instructora del Garden, contra {name}!"),
					N("Regras: Same, Plus e Combo. Em jogo: 1 carta do deck de Xu. A final da seletiva espera na próxima cadeira.",
						"Rules: Same, Plus and Combo. At stake: 1 card from Xu's deck. The qualifier final waits in the next chair.",
						"Reglas: Same, Plus y Combo. En juego: 1 carta del mazo de Xu. La final de la selectiva espera en la siguiente silla.")
					},
					say("cc_heart", "Xu", "Xu", "Xu",
						"Eu não apresso Plus. Coloque com calma, ou eu fecho a soma.",
						"I don't rush Plus. Place it calmly, or I'll close the sum.",
						"No apuro el Plus. Coloca con calma, o cierro la suma."),
					lost("cc_heart", "Xu", "Xu", "Xu",
						"Leitura limpa. Você pode sentar na final da seletiva.",
						"Clean reading. You can sit in the qualifier final.",
						"Lectura limpia. Puedes sentarte en la final de la selectiva."),
					say("cc_heart", "Xu", "Xu", "Xu",
						"A soma era minha. Reveja o tabuleiro e volte.",
						"The sum was mine. Recheck the board and come back.",
						"La suma era mía. Revisa el tablero y vuelve."),
					new Line[] {
					N("Vitória de {name}! Escolha 1 carta do deck que você acabou de enfrentar. A derrota não teria tirado nada.",
						"Win for {name}! Choose 1 card from the deck you just faced. A loss would have taken nothing.",
						"¡Victoria de {name}! Elige 1 carta del mazo que acabas de enfrentar. La derrota no habría quitado nada.")
					},
					new Line[] {
					N("Derrota de {name}. Reconhecimento esportivo: a mesa fica aberta. Pode tentar de novo, sem perder carta.",
						"Defeat for {name}. A sportsmanlike reading: the table stays open. You may try again, and you lose no card.",
						"Derrota de {name}. Reconocimiento deportivo: la mesa sigue abierta. Puedes intentarlo de nuevo, sin perder carta.")
					}),
				duel("king", "cc_king", "Quistis", "Quistis", "Quistis", 4, 5,
					Options.AIType.BALANCED, Ruleset.PLUS, 85, false,
					"Instrutora do Garden", "Garden instructor", "Instructora del Garden",
					"Chefe da seletiva. Plus preciso e cartas mais altas.", "Qualifier boss. Precise Plus and higher cards.", "Jefa de la selectiva. Plus preciso y cartas más altas.",
					new Line[] {
					N("Quistis, instrutora de Balamb Garden. Final da seletiva e chefe desta etapa.",
						"Quistis, a Balamb Garden instructor. Qualifier final and boss of this stage.",
						"Quistis, instructora de Balamb Garden. Final de la selectiva y jefa de esta etapa."),
					N("Same, Plus e Combo. Se {name} vencer: Ifrit, carta fixa, e mais 1 carta do deck dela.",
						"Same, Plus and Combo. If {name} wins: Ifrit, the fixed card, plus 1 more from her deck.",
						"Same, Plus y Combo. Si {name} gana: Ifrit, carta fija, y 1 carta más de su mazo.")
					},
					new Line[] {
					N("Seletiva do Garden, final! Quistis, instrutora do Garden, contra {name}!",
						"Garden qualifier, final! Quistis, Garden instructor, against {name}!",
						"¡Selectiva del Garden, final! ¡Quistis, instructora del Garden, contra {name}!"),
					N("Regras: Same, Plus e Combo. Chefe da etapa: Ifrit como carta fixa, mais 1 carta do deck de Quistis. A vaga no mundial sai só com a vitória.",
						"Rules: Same, Plus and Combo. Stage boss: Ifrit as the fixed card, plus 1 card from Quistis's deck. The world-tournament spot comes only with a win.",
						"Reglas: Same, Plus y Combo. Jefa de la etapa: Ifrit como carta fija, más 1 carta del mazo de Quistis. La plaza del mundial solo sale con la victoria.")
					},
					say("cc_king", "Quistis", "Quistis", "Quistis",
						"Eu não vou facilitar o Plus. Feche a soma ou perca a mesa.",
						"I won't make Plus easy. Close the sum or lose the table.",
						"No voy a facilitar el Plus. Cierra la suma o pierde la mesa."),
					lost("cc_king", "Quistis", "Quistis", "Quistis",
						"Você segurou o Combo até o fim. Leve Ifrit e escolha mais uma.",
						"You held the Combo to the end. Take Ifrit and pick one more.",
						"Aguantaste el Combo hasta el final. Llévate a Ifrit y elige una más."),
					say("cc_king", "Quistis", "Quistis", "Quistis",
						"A seletiva não elimina. Corrija a soma e sente de novo.",
						"The qualifier doesn't eliminate you. Fix the sum and sit again.",
						"La selectiva no te elimina. Corrige la suma y siéntate de nuevo."),
					new Line[] {
					N("Vitória de {name}! {O campeão|A campeã} da mesa leva a carta fixa deste chefe e ainda escolhe mais uma no deck adversário.",
						"Win for {name}! The table winner takes this boss's fixed card and also picks one more from the opponent's deck.",
						"¡Victoria de {name}! {El campeón|La campeona} de la mesa se lleva la carta fija de este jefe y elige una más del mazo rival."),
					Q("No porto a tabela vale 3 pontos por vitória. Não desperdice uma soma.",
						"At the harbor the table is worth 3 points a win. Don't waste a sum.",
						"En el puerto la tabla vale 3 puntos por victoria. No desperdicies una suma.")
					},
					new Line[] {
					N("Derrota de {name}. Reconhecimento esportivo: a mesa fica aberta. Pode tentar de novo, sem perder carta.",
						"Defeat for {name}. A sportsmanlike reading: the table stays open. You may try again, and you lose no card.",
						"Derrota de {name}. Reconocimiento deportivo: la mesa sigue abierta. Puedes intentarlo de nuevo, sin perder carta."),
					Q("A final da seletiva tem revanche na hora. A vaga só sai com a vitória.",
						"The qualifier final has an instant rematch. The spot only comes with a win.",
						"La final de la selectiva tiene revancha al momento. La plaza solo llega con la victoria.")
					})
			});
	}

	private static Region town() {
		return region("town", "chegada_balamb_town", "mesa_balamb_town.png", Kind.GROUP, 2,
			new String[][] {{"queen_town", "raijin"}, {"queen_town", "fujin"}, {"raijin", "fujin"}},
			"Balamb - Classificatória", "Balamb - Qualifying", "Balamb - Clasificatoria",
			new Line[] {
				N("O porto de Balamb sedia o Grupo do Porto. Cada vitória vale 3 pontos. Cortam os 2 primeiros.",
					"Balamb harbor hosts the Harbor Group. Each win is worth 3 points. Top 2 go through.",
					"El puerto de Balamb acoge el Grupo del Puerto. Cada victoria vale 3 puntos. Pasan los 2 primeros."),
				Q("Plus continua. A tabela não é aquecimento. Três pontos por mesa fechada.",
					"Plus is still on. The standings are not a warm-up. Three points for a closed table.",
					"Plus sigue. La tabla no es un calentamiento. Tres puntos por mesa cerrada.")
			},
			new Duel[] {
				duel("queen_town", "card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas", 3, 5,
					Options.AIType.BALANCED, Ruleset.PLUS, 0, true,
					"Viajante da mesa", "Table traveler", "Viajera de la mesa",
					"Plus de grupo. Ela joga para a tabela do porto.", "Group Plus. She plays for the harbor table.", "Plus de grupo. Ella juega para la tabla del puerto.",
					new Line[] {
					N("A Rainha das Cartas, de passagem pelo porto de Balamb. Primeira mesa do grupo.",
						"The Queen of Cards, passing through Balamb harbor. First table of the group.",
						"La Reina de las Cartas, de paso por el puerto de Balamb. Primera mesa del grupo."),
					N("Same, Plus e Combo. Vitória vale 3 pontos. Recompensa: 1 carta do deck dela.",
						"Same, Plus and Combo. A win is worth 3 points. Prize: 1 card from her deck.",
						"Same, Plus y Combo. La victoria vale 3 puntos. Recompensa: 1 carta de su mazo.")
					},
					new Line[] {
					N("Grupo do Porto, mesa 1 de 3! A Rainha das Cartas, de passagem por Balamb, contra {name}!",
						"Harbor Group, table 1 of 3! The Queen of Cards, passing through Balamb, against {name}!",
						"¡Grupo del Puerto, mesa 1 de 3! ¡La Reina de las Cartas, de paso por Balamb, contra {name}!"),
					N("Regras: Same, Plus e Combo. Em jogo: 1 carta do deck da Rainha. Vitória vale 3 pontos na tabela. Cortam os 2 primeiros.",
						"Rules: Same, Plus and Combo. At stake: 1 card from the Queen's deck. A win is worth 3 points in the standings. Top 2 go through.",
						"Reglas: Same, Plus y Combo. En juego: 1 carta del mazo de la Reina. La victoria vale 3 puntos en la tabla. Pasan los 2 primeros.")
					},
					say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"Três pontos nesta mesa. Não trate Plus como aquecimento.",
						"Three points at this table. Don't treat Plus as a warm-up.",
						"Tres puntos en esta mesa. No tomes Plus como un calentamiento."),
					lost("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"Você fechou a soma e levou os 3 pontos. Escolha a carta.",
						"You closed the sum and took the 3 points. Pick the card.",
						"Cerraste la suma y te llevaste los 3 puntos. Elige la carta."),
					say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"A tabela não me espera. Volte quando a soma estiver pronta.",
						"The standings won't wait for me. Come back when the sum is ready.",
						"La tabla no me espera. Vuelve cuando la suma esté lista."),
					new Line[] {
					N("Vitória de {name}! Escolha 1 carta do deck que você acabou de enfrentar. A derrota não teria tirado nada.",
						"Win for {name}! Choose 1 card from the deck you just faced. A loss would have taken nothing.",
						"¡Victoria de {name}! Elige 1 carta del mazo que acabas de enfrentar. La derrota no habría quitado nada.")
					},
					new Line[] {
					N("Derrota de {name}. Reconhecimento esportivo: a mesa fica aberta. Pode tentar de novo, sem perder carta.",
						"Defeat for {name}. A sportsmanlike reading: the table stays open. You may try again, and you lose no card.",
						"Derrota de {name}. Reconocimiento deportivo: la mesa sigue abierta. Puedes intentarlo de nuevo, sin perder carta.")
					}),
				duel("raijin", "raijin", "Raijin", "Raijin", "Raijin", 4, 5,
					Options.AIType.BALANCED, Ruleset.PLUS, 0, true,
					"Discípulo de Balamb", "Balamb disciple", "Discípulo de Balamb",
					"Plus pesado. Ele empurra o tabuleiro, ya know.", "Heavy Plus. He shoves the board, ya know.", "Plus pesado. Empuja el tablero, ya know.",
					new Line[] {
					N("Raijin, de Balamb. Segunda mesa do grupo do porto.",
						"Raijin, from Balamb. Second table of the harbor group.",
						"Raijin, de Balamb. Segunda mesa del grupo del puerto."),
					N("Same, Plus e Combo. Recompensa: 1 carta do deck dele. Vitória vale 3 pontos.",
						"Same, Plus and Combo. Prize: 1 card from his deck. A win is worth 3 points.",
						"Same, Plus y Combo. Recompensa: 1 carta de su mazo. La victoria vale 3 puntos.")
					},
					new Line[] {
					N("Grupo do Porto, mesa 2 de 3! Raijin, de Balamb, contra {name}!",
						"Harbor Group, table 2 of 3! Raijin, from Balamb, against {name}!",
						"¡Grupo del Puerto, mesa 2 de 3! ¡Raijin, de Balamb, contra {name}!"),
					N("Regras: Same, Plus e Combo. Em jogo: 1 carta do deck de Raijin. Mais 3 pontos na tabela se {name} vencer.",
						"Rules: Same, Plus and Combo. At stake: 1 card from Raijin's deck. 3 more points in the standings if {name} wins.",
						"Reglas: Same, Plus y Combo. En juego: 1 carta del mazo de Raijin. 3 puntos más en la tabla si {name} gana.")
					},
					say("raijin", "Raijin", "Raijin", "Raijin",
						"I'm goin' in, ya know. Segura o Plus se puder.",
						"I'm goin' in, ya know. Hold that Plus if you can.",
						"I'm goin' in, ya know. Aguanta el Plus si puedes."),
					lost("raijin", "Raijin", "Raijin", "Raijin",
						"You got me, ya know. Leva a carta e os pontos.",
						"You got me, ya know. Take the card and the points.",
						"You got me, ya know. Llévate la carta y los puntos."),
					say("raijin", "Raijin", "Raijin", "Raijin",
						"That's how it goes, ya know. A mesa ficou comigo.",
						"That's how it goes, ya know. The table stayed with me.",
						"That's how it goes, ya know. La mesa se quedó conmigo."),
					new Line[] {
					N("Vitória de {name}! Escolha 1 carta do deck que você acabou de enfrentar. A derrota não teria tirado nada.",
						"Win for {name}! Choose 1 card from the deck you just faced. A loss would have taken nothing.",
						"¡Victoria de {name}! Elige 1 carta del mazo que acabas de enfrentar. La derrota no habría quitado nada.")
					},
					new Line[] {
					N("Derrota de {name}. Reconhecimento esportivo: a mesa fica aberta. Pode tentar de novo, sem perder carta.",
						"Defeat for {name}. A sportsmanlike reading: the table stays open. You may try again, and you lose no card.",
						"Derrota de {name}. Reconocimiento deportivo: la mesa sigue abierta. Puedes intentarlo de nuevo, sin perder carta.")
					}),
				duel("fujin", "fujin", "Fujin", "Fujin", "Fujin", 5, 6,
					Options.AIType.OFFENSIVE, Ruleset.PLUS, 83, true,
					"Discípula de Balamb", "Balamb disciple", "Discípula de Balamb",
					"CHEFE DO PORTO. PLUS RÁPIDO, CARTAS MAIS ALTAS.", "HARBOR BOSS. FAST PLUS, HIGHER CARDS.", "JEFA DEL PUERTO. PLUS RÁPIDO, CARTAS MÁS ALTAS.",
					new Line[] {
					N("Fujin, de Balamb. Chefe do grupo do porto.",
						"Fujin, from Balamb. Boss of the harbor group.",
						"Fujin, de Balamb. Jefa del grupo del puerto."),
					N("Same, Plus e Combo. Se {name} vencer: Quezacotl, carta fixa, e mais 1 carta do deck dela.",
						"Same, Plus and Combo. If {name} wins: Quezacotl, the fixed card, plus 1 more from her deck.",
						"Same, Plus y Combo. Si {name} gana: Quezacotl, carta fija, y 1 carta más de su mazo.")
					},
					new Line[] {
					N("Grupo do Porto, mesa 3 de 3, chefe da etapa! Fujin, de Balamb, contra {name}!",
						"Harbor Group, table 3 of 3, stage boss! Fujin, from Balamb, against {name}!",
						"¡Grupo del Puerto, mesa 3 de 3, jefa de la etapa! ¡Fujin, de Balamb, contra {name}!"),
					N("Regras: Same, Plus e Combo. Chefe: Quezacotl como carta fixa, mais 1 carta do deck de Fujin. Cortam os 2 primeiros.",
						"Rules: Same, Plus and Combo. Boss: Quezacotl as the fixed card, plus 1 card from Fujin's deck. Top 2 go through.",
						"Reglas: Same, Plus y Combo. Jefa: Quezacotl como carta fija, más 1 carta del mazo de Fujin. Pasan los 2 primeros.")
					},
					say("fujin", "Fujin", "Fujin", "Fujin",
						"PLUS. NÃO ERRE A SOMA.",
						"PLUS. DON'T MISS THE SUM.",
						"PLUS. NO FALLES LA SUMA."),
					lost("fujin", "Fujin", "Fujin", "Fujin",
						"VOCÊ FECHOU. LEVE QUEZACOTL E ESCOLHA MAIS UMA.",
						"YOU CLOSED IT. TAKE QUEZACOTL AND PICK ONE MORE.",
						"LO CERRISTE. LLÉVATE A QUEZACOTL Y ELIGE UNA MÁS."),
					say("fujin", "Fujin", "Fujin", "Fujin",
						"A SOMA FOI MINHA. VOLTE.",
						"THE SUM WAS MINE. COME BACK.",
						"LA SUMA FUE MÍA. VUELVE."),
					new Line[] {
					N("Vitória de {name}! {O campeão|A campeã} da mesa leva a carta fixa deste chefe e ainda escolhe mais uma no deck adversário.",
						"Win for {name}! The table winner takes this boss's fixed card and also picks one more from the opponent's deck.",
						"¡Victoria de {name}! {El campeón|La campeona} de la mesa se lleva la carta fija de este jefe y elige una más del mazo rival.")
					},
					new Line[] {
					N("Derrota de {name}. Reconhecimento esportivo: a mesa fica aberta. Pode tentar de novo, sem perder carta.",
						"Defeat for {name}. A sportsmanlike reading: the table stays open. You may try again, and you lose no card.",
						"Derrota de {name}. Reconocimiento deportivo: la mesa sigue abierta. Puedes intentarlo de nuevo, sin perder carta.")
					})
			});
	}

	private static Region timber() {
		return region("timber", "chegada_timber", "mesa_timber.png", Kind.GROUP, 3,
			new String[][] {
				{"rinoa", "zone"}, {"rinoa", "jack"}, {"rinoa", "soldier_timber"},
				{"jack", "zone"}, {"jack", "soldier_timber"}, {"zone", "soldier_timber"}
			},
			"Timber - Grupos", "Timber - Groups", "Timber - Grupos",
			new Line[] {
				N("A estação de Timber sedia o grupo da ferrovia. Cada vitória vale 3 pontos. Cortam os 3 primeiros.",
					"Timber station hosts the railway group. Each win is worth 3 points. Top 3 go through.",
					"La estación de Timber acoge el grupo del ferrocarril. Cada victoria vale 3 puntos. Pasan los 3 primeros."),
				Q("Elemental entra nesta cidade. Olhe o chão antes de soltar a carta.",
					"Elemental arrives in this city. Look at the floor before you drop a card.",
					"Elemental entra en esta ciudad. Mira el suelo antes de soltar la carta.")
			},
			new Duel[] {
				duel("zone", "zone_watts", "Zone e Watts", "Zone and Watts", "Zone y Watts", 4, 5,
					Options.AIType.BALANCED, Ruleset.PLUS, 0, true,
					"Repórteres de Timber", "Timber reporters", "Reporteros de Timber",
					"Plus de rua. Eles falam alto e jogam rápido.", "Street Plus. They talk loud and play fast.", "Plus de calle. Hablan alto y juegan rápido.",
					new Line[] {
					N("Zone e Watts, de Timber. Primeira mesa do grupo da estação.",
						"Zone and Watts, from Timber. First table of the station group.",
						"Zone y Watts, de Timber. Primera mesa del grupo de la estación."),
					N("Same, Plus e Combo. Recompensa: 1 carta do deck deles. Cortam os 3 primeiros.",
						"Same, Plus and Combo. Prize: 1 card from their deck. Top 3 go through.",
						"Same, Plus y Combo. Recompensa: 1 carta de su mazo. Pasan los 3 primeros.")
					},
					new Line[] {
					N("Grupo de Timber, mesa 1 de 4! Zone e Watts, da estação, contra {name}!",
						"Timber Group, table 1 of 4! Zone and Watts, from the station, against {name}!",
						"¡Grupo de Timber, mesa 1 de 4! ¡Zone y Watts, de la estación, contra {name}!"),
					N("Regras: Same, Plus e Combo. Em jogo: 1 carta do deck de Zone e Watts. Vitória vale 3 pontos. Cortam os 3 primeiros.",
						"Rules: Same, Plus and Combo. At stake: 1 card from Zone and Watts's deck. A win is worth 3 points. Top 3 go through.",
						"Reglas: Same, Plus y Combo. En juego: 1 carta del mazo de Zone y Watts. La victoria vale 3 puntos. Pasan los 3 primeros.")
					},
					say("zone_watts", "Zone e Watts", "Zone and Watts", "Zone y Watts",
						"Câmera ligada! Fecha o Plus na nossa cara se puder.",
						"Camera's rolling! Close Plus in our face if you can.",
						"¡Cámara en marcha! Cierra el Plus en nuestra cara si puedes."),
					lost("zone_watts", "Zone e Watts", "Zone and Watts", "Zone y Watts",
						"Boa fita. Leva a carta, a gente edita o resto.",
						"Nice tape. Take the card, we'll edit the rest.",
						"Buena cinta. Llévate la carta, nosotros editamos el resto."),
					say("zone_watts", "Zone e Watts", "Zone and Watts", "Zone y Watts",
						"A gente filmou o erro. Volta pra outra tomada.",
						"We filmed the miss. Come back for another take.",
						"Filmamos el error. Vuelve para otra toma."),
					new Line[] {
					N("Vitória de {name}! Escolha 1 carta do deck que você acabou de enfrentar. A derrota não teria tirado nada.",
						"Win for {name}! Choose 1 card from the deck you just faced. A loss would have taken nothing.",
						"¡Victoria de {name}! Elige 1 carta del mazo que acabas de enfrentar. La derrota no habría quitado nada.")
					},
					new Line[] {
					N("Derrota de {name}. Reconhecimento esportivo: a mesa fica aberta. Pode tentar de novo, sem perder carta.",
						"Defeat for {name}. A sportsmanlike reading: the table stays open. You may try again, and you lose no card.",
						"Derrota de {name}. Reconocimiento deportivo: la mesa sigue abierta. Puedes intentarlo de nuevo, sin perder carta.")
					}),
				duel("jack", "cc_jack", "Edd Brask", "Edd Brask", "Edd Brask", 4, 6,
					Options.AIType.BALANCED, Ruleset.ELEMENT, 0, true,
					"Ferroviário de Timber", "Timber rail worker", "Ferroviario de Timber",
					"Elemental entra aqui, com Same, Plus e Combo.", "Elemental arrives here, with Same, Plus and Combo.", "Elemental entra aquí, con Same, Plus y Combo.",
					new Line[] {
					N("Edd Brask, ferroviário de Timber. Segunda mesa do grupo.",
						"Edd Brask, a Timber rail worker. Second table of the group.",
						"Edd Brask, ferroviario de Timber. Segunda mesa del grupo."),
					N("Elemental: o chão dá +1 ou -1. Recompensa: 1 carta do deck dele.",
						"Elemental: the floor gives +1 or -1. Prize: 1 card from his deck.",
						"Elemental: el suelo da +1 o -1. Recompensa: 1 carta de su mazo.")
					},
					new Line[] {
					N("Grupo de Timber, mesa 2 de 4! Edd Brask, ferroviário de Timber, contra {name}!",
						"Timber Group, table 2 of 4! Edd Brask, Timber rail worker, against {name}!",
						"¡Grupo de Timber, mesa 2 de 4! ¡Edd Brask, ferroviario de Timber, contra {name}!"),
					N("Regras: Same, Plus, Combo e Elemental. Em jogo: 1 carta do deck de Edd. Olhe o elemento do chão antes de soltar a carta.",
						"Rules: Same, Plus, Combo and Elemental. At stake: 1 card from Edd's deck. Check the floor element before you drop a card.",
						"Reglas: Same, Plus, Combo y Elemental. En juego: 1 carta del mazo de Edd. Mira el elemento del suelo antes de soltar la carta.")
					},
					say("cc_jack", "Edd Brask", "Edd Brask", "Edd Brask",
						"O chão desta estação não é neutro. Leia o elemento.",
						"The floor of this station is not neutral. Read the element.",
						"El suelo de esta estación no es neutro. Lee el elemento."),
					lost("cc_jack", "Edd Brask", "Edd Brask", "Edd Brask",
						"Você usou o chão a seu favor. A carta é sua.",
						"You used the floor in your favor. The card is yours.",
						"Usaste el suelo a tu favor. La carta es tuya."),
					say("cc_jack", "Edd Brask", "Edd Brask", "Edd Brask",
						"O -1 te derrubou. Olhe o chão e volte.",
						"The -1 brought you down. Look at the floor and come back.",
						"El -1 te derribó. Mira el suelo y vuelve."),
					new Line[] {
					N("Vitória de {name}! Escolha 1 carta do deck que você acabou de enfrentar. A derrota não teria tirado nada.",
						"Win for {name}! Choose 1 card from the deck you just faced. A loss would have taken nothing.",
						"¡Victoria de {name}! Elige 1 carta del mazo que acabas de enfrentar. La derrota no habría quitado nada."),
					Q("Elemental: mesmo elemento, +1 naquele lado; outro, -1. O chão decide antes da comparação.",
						"Elemental: same element, +1 on that side; a different one, -1. The floor decides before the compare.",
						"Elemental: mismo elemento, +1 en ese lado; otro, -1. El suelo decide antes de la comparación.")
					},
					new Line[] {
					N("Derrota de {name}. Reconhecimento esportivo: a mesa fica aberta. Pode tentar de novo, sem perder carta.",
						"Defeat for {name}. A sportsmanlike reading: the table stays open. You may try again, and you lose no card.",
						"Derrota de {name}. Reconocimiento deportivo: la mesa sigue abierta. Puedes intentarlo de nuevo, sin perder carta.")
					}),
				duel("soldier_timber", "npc_soldado_galbadia", "Bram Holt", "Bram Holt", "Bram Holt", 5, 6,
					Options.AIType.OFFENSIVE, Ruleset.PLUS, 0, true,
					"Soldado de Galbadia", "Galbadian soldier", "Soldado de Galbadia",
					"Plus ofensivo, sem Elemental nesta mesa.", "Offensive Plus, no Elemental at this table.", "Plus ofensivo, sin Elemental en esta mesa.",
					new Line[] {
					N("Bram Holt, soldado de Galbadia destacado em Timber. Terceira mesa do grupo.",
						"Bram Holt, a Galbadian soldier posted in Timber. Third table of the group.",
						"Bram Holt, soldado de Galbadia destacado en Timber. Tercera mesa del grupo."),
					N("Same, Plus e Combo. Recompensa: 1 carta do deck dele.",
						"Same, Plus and Combo. Prize: 1 card from his deck.",
						"Same, Plus y Combo. Recompensa: 1 carta de su mazo.")
					},
					new Line[] {
					N("Grupo de Timber, mesa 3 de 4! Bram Holt, soldado de Galbadia, contra {name}!",
						"Timber Group, table 3 of 4! Bram Holt, Galbadian soldier, against {name}!",
						"¡Grupo de Timber, mesa 3 de 4! ¡Bram Holt, soldado de Galbadia, contra {name}!"),
					N("Regras: Same, Plus e Combo. Em jogo: 1 carta do deck de Bram. Vitória vale 3 pontos.",
						"Rules: Same, Plus and Combo. At stake: 1 card from Bram's deck. A win is worth 3 points.",
						"Reglas: Same, Plus y Combo. En juego: 1 carta del mazo de Bram. La victoria vale 3 puntos.")
					},
					say("npc_soldado_galbadia", "Bram Holt", "Bram Holt", "Bram Holt",
						"Ordem é ordem. Eu empurro o Plus. Aguente se puder.",
						"Orders are orders. I'll push Plus. Hold it if you can.",
						"Las órdenes son órdenes. Empujo el Plus. Aguanta si puedes."),
					lost("npc_soldado_galbadia", "Bram Holt", "Bram Holt", "Bram Holt",
						"Você segurou a linha. Leve a carta.",
						"You held the line. Take the card.",
						"Aguantaste la línea. Llévate la carta."),
					say("npc_soldado_galbadia", "Bram Holt", "Bram Holt", "Bram Holt",
						"A linha caiu. Reforme e sente de novo.",
						"The line fell. Reform and sit again.",
						"La línea cayó. Reorganiza y siéntate de nuevo."),
					new Line[] {
					N("Vitória de {name}! Escolha 1 carta do deck que você acabou de enfrentar. A derrota não teria tirado nada.",
						"Win for {name}! Choose 1 card from the deck you just faced. A loss would have taken nothing.",
						"¡Victoria de {name}! Elige 1 carta del mazo que acabas de enfrentar. La derrota no habría quitado nada.")
					},
					new Line[] {
					N("Derrota de {name}. Reconhecimento esportivo: a mesa fica aberta. Pode tentar de novo, sem perder carta.",
						"Defeat for {name}. A sportsmanlike reading: the table stays open. You may try again, and you lose no card.",
						"Derrota de {name}. Reconocimiento deportivo: la mesa sigue abierta. Puedes intentarlo de nuevo, sin perder carta.")
					}),
				duel("rinoa", "rinoa", "Rinoa", "Rinoa", "Rinoa", 5, 7,
					Options.AIType.BALANCED, Ruleset.ELEMENT, 86, true,
					"Visitante de Timber", "Timber visitor", "Visitante de Timber",
					"Chefe da estação. Elemental e cartas mais altas.", "Station boss. Elemental and higher cards.", "Jefa de la estación. Elemental y cartas más altas.",
					new Line[] {
					N("Rinoa, de visita em Timber. Chefe do grupo da estação.",
						"Rinoa, visiting Timber. Boss of the station group.",
						"Rinoa, de visita en Timber. Jefa del grupo de la estación."),
					N("Same, Plus, Combo e Elemental. Se {name} vencer: Siren, carta fixa, e mais 1 carta do deck dela.",
						"Same, Plus, Combo and Elemental. If {name} wins: Siren, the fixed card, plus 1 more from her deck.",
						"Same, Plus, Combo y Elemental. Si {name} gana: Siren, carta fija, y 1 carta más de su mazo.")
					},
					new Line[] {
					N("Grupo de Timber, mesa 4 de 4, chefe da etapa! Rinoa, de visita na estação, contra {name}!",
						"Timber Group, table 4 of 4, stage boss! Rinoa, visiting the station, against {name}!",
						"¡Grupo de Timber, mesa 4 de 4, jefa de la etapa! ¡Rinoa, de visita en la estación, contra {name}!"),
					N("Regras: Same, Plus, Combo e Elemental. Chefe: Siren como carta fixa, mais 1 carta do deck de Rinoa. Cortam os 3 primeiros.",
						"Rules: Same, Plus, Combo and Elemental. Boss: Siren as the fixed card, plus 1 card from Rinoa's deck. Top 3 go through.",
						"Reglas: Same, Plus, Combo y Elemental. Jefa: Siren como carta fija, más 1 carta del mazo de Rinoa. Pasan los 3 primeros.")
					},
					say("rinoa", "Rinoa", "Rinoa", "Rinoa",
						"Olhe o chão comigo. Elemental decide o lado fino.",
						"Watch the floor with me. Elemental decides the fine side.",
						"Mira el suelo conmigo. Elemental decide el lado fino."),
					lost("rinoa", "Rinoa", "Rinoa", "Rinoa",
						"Você leu o chão até o fim. Leve Siren e escolha mais uma.",
						"You read the floor to the end. Take Siren and pick one more.",
						"Leíste el suelo hasta el final. Llévate a Siren y elige una más."),
					say("rinoa", "Rinoa", "Rinoa", "Rinoa",
						"O elemento te puxou. Volte quando estiver {pronto|pronta}.",
						"The element pulled you. Come back when you're ready.",
						"El elemento te empujó. Vuelve cuando estés {listo|lista}."),
					new Line[] {
					N("Vitória de {name}! {O campeão|A campeã} da mesa leva a carta fixa deste chefe e ainda escolhe mais uma no deck adversário.",
						"Win for {name}! The table winner takes this boss's fixed card and also picks one more from the opponent's deck.",
						"¡Victoria de {name}! {El campeón|La campeona} de la mesa se lleva la carta fija de este jefe y elige una más del mazo rival.")
					},
					new Line[] {
					N("Derrota de {name}. Reconhecimento esportivo: a mesa fica aberta. Pode tentar de novo, sem perder carta.",
						"Defeat for {name}. A sportsmanlike reading: the table stays open. You may try again, and you lose no card.",
						"Derrota de {name}. Reconocimiento deportivo: la mesa sigue abierta. Puedes intentarlo de nuevo, sin perder carta.")
					})
			});
	}

	private static Region deling() {
		return region("deling", "chegada_deling", "mesa_deling.png", Kind.GROUP, 2,
			new String[][] {{"queen_deling", "diamond"}, {"queen_deling", "soldier_deling"}, {"diamond", "soldier_deling"}},
			"Deling - Fase principal", "Deling - Main stage", "Deling - Fase principal",
			new Line[] {
				N("Timber mandou os 3 primeiros. {name} passou. Deling sedia a fase principal. Cortam os 2 primeiros.",
					"Timber sent the top 3. {name} made it. Deling hosts the main stage. Top 2 go through.",
					"Timber mandó a los 3 primeros. {name} pasó. Deling acoge la fase principal. Pasan los 2 primeros."),
				Q("Elemental segue na capital. Não ignore o chão.",
					"Elemental stays on in the capital. Don't ignore the floor.",
					"Elemental sigue en la capital. No ignores el suelo.")
			},
			new Duel[] {
				duel("diamond", "cc_diamond", "Hal Vektor", "Hal Vektor", "Hal Vektor", 5, 6,
					Options.AIType.BALANCED, Ruleset.ELEMENT, 0, true,
					"Oficial de Galbadia", "Galbadian officer", "Oficial de Galbadia",
					"Elemental de capital. Jogo formal e frio.", "Capital Elemental. Formal, cold play.", "Elemental de capital. Juego formal y frío.",
					new Line[] {
					N("Hal Vektor, oficial de Galbadia em Deling. Primeira mesa do grupo da capital.",
						"Hal Vektor, a Galbadian officer in Deling. First table of the capital group.",
						"Hal Vektor, oficial de Galbadia en Deling. Primera mesa del grupo de la capital."),
					N("Same, Plus, Combo e Elemental. Recompensa: 1 carta do deck dele. Cortam os 2 primeiros.",
						"Same, Plus, Combo and Elemental. Prize: 1 card from his deck. Top 2 go through.",
						"Same, Plus, Combo y Elemental. Recompensa: 1 carta de su mazo. Pasan los 2 primeros.")
					},
					new Line[] {
					N("Grupo de Deling, mesa 1 de 3! Hal Vektor, oficial de Galbadia, contra {name}!",
						"Deling Group, table 1 of 3! Hal Vektor, Galbadian officer, against {name}!",
						"¡Grupo de Deling, mesa 1 de 3! ¡Hal Vektor, oficial de Galbadia, contra {name}!"),
					N("Regras: Same, Plus, Combo e Elemental. Em jogo: 1 carta do deck de Hal. Vitória vale 3 pontos. Cortam os 2 primeiros.",
						"Rules: Same, Plus, Combo and Elemental. At stake: 1 card from Hal's deck. A win is worth 3 points. Top 2 go through.",
						"Reglas: Same, Plus, Combo y Elemental. En juego: 1 carta del mazo de Hal. La victoria vale 3 puntos. Pasan los 2 primeros.")
					},
					say("cc_diamond", "Hal Vektor", "Hal Vektor", "Hal Vektor",
						"Protocolo da capital: leia o elemento, depois solte a carta.",
						"Capital protocol: read the element, then drop the card.",
						"Protocolo de la capital: lee el elemento, luego suelta la carta."),
					lost("cc_diamond", "Hal Vektor", "Hal Vektor", "Hal Vektor",
						"Protocolo cumprido. A carta passa para {name}.",
						"Protocol observed. The card goes to {name}.",
						"Protocolo cumplido. La carta pasa a {name}."),
					say("cc_diamond", "Hal Vektor", "Hal Vektor", "Hal Vektor",
						"O protocolo te atrasou. Repita a mesa.",
						"Protocol slowed you down. Repeat the table.",
						"El protocolo te atrasó. Repite la mesa."),
					new Line[] {
					N("Vitória de {name}! Escolha 1 carta do deck que você acabou de enfrentar. A derrota não teria tirado nada.",
						"Win for {name}! Choose 1 card from the deck you just faced. A loss would have taken nothing.",
						"¡Victoria de {name}! Elige 1 carta del mazo que acabas de enfrentar. La derrota no habría quitado nada.")
					},
					new Line[] {
					N("Derrota de {name}. Reconhecimento esportivo: a mesa fica aberta. Pode tentar de novo, sem perder carta.",
						"Defeat for {name}. A sportsmanlike reading: the table stays open. You may try again, and you lose no card.",
						"Derrota de {name}. Reconocimiento deportivo: la mesa sigue abierta. Puedes intentarlo de nuevo, sin perder carta.")
					}),
				duel("soldier_deling", "npc_soldado_galbadia", "Mara Quill", "Mara Quill", "Mara Quill", 6, 7,
					Options.AIType.OFFENSIVE, Ruleset.ELEMENT, 0, true,
					"Oficial de Galbadia", "Galbadian officer", "Oficial de Galbadia",
					"Elemental ofensivo, um degrau acima de Hal.", "Offensive Elemental, a step above Hal.", "Elemental ofensivo, un peldaño por encima de Hal.",
					new Line[] {
					N("Mara Quill, oficial de Galbadia em Deling. Segunda mesa do grupo.",
						"Mara Quill, a Galbadian officer in Deling. Second table of the group.",
						"Mara Quill, oficial de Galbadia en Deling. Segunda mesa del grupo."),
					N("Same, Plus, Combo e Elemental. Recompensa: 1 carta do deck dela.",
						"Same, Plus, Combo and Elemental. Prize: 1 card from her deck.",
						"Same, Plus, Combo y Elemental. Recompensa: 1 carta de su mazo.")
					},
					new Line[] {
					N("Grupo de Deling, mesa 2 de 3! Mara Quill, oficial de Galbadia, contra {name}!",
						"Deling Group, table 2 of 3! Mara Quill, Galbadian officer, against {name}!",
						"¡Grupo de Deling, mesa 2 de 3! ¡Mara Quill, oficial de Galbadia, contra {name}!"),
					N("Regras: Same, Plus, Combo e Elemental. Em jogo: 1 carta do deck de Mara. A chefe da etapa vem a seguir.",
						"Rules: Same, Plus, Combo and Elemental. At stake: 1 card from Mara's deck. The stage boss comes next.",
						"Reglas: Same, Plus, Combo y Elemental. En juego: 1 carta del mazo de Mara. La jefa de la etapa viene a continuación.")
					},
					say("npc_soldado_galbadia", "Mara Quill", "Mara Quill", "Mara Quill",
						"Eu ataco o chão e o lado. Não durma no Elemental.",
						"I'll attack the floor and the side. Don't sleep on Elemental.",
						"Ataco el suelo y el lado. No te duermas en el Elemental."),
					lost("npc_soldado_galbadia", "Mara Quill", "Mara Quill", "Mara Quill",
						"Você segurou o ataque. Leve a carta.",
						"You held the attack. Take the card.",
						"Aguantaste el ataque. Llévate la carta."),
					say("npc_soldado_galbadia", "Mara Quill", "Mara Quill", "Mara Quill",
						"O lado caiu. Ajuste o elemento e volte.",
						"The side fell. Adjust the element and come back.",
						"El lado cayó. Ajusta el elemento y vuelve."),
					new Line[] {
					N("Vitória de {name}! Escolha 1 carta do deck que você acabou de enfrentar. A derrota não teria tirado nada.",
						"Win for {name}! Choose 1 card from the deck you just faced. A loss would have taken nothing.",
						"¡Victoria de {name}! Elige 1 carta del mazo que acabas de enfrentar. La derrota no habría quitado nada.")
					},
					new Line[] {
					N("Derrota de {name}. Reconhecimento esportivo: a mesa fica aberta. Pode tentar de novo, sem perder carta.",
						"Defeat for {name}. A sportsmanlike reading: the table stays open. You may try again, and you lose no card.",
						"Derrota de {name}. Reconocimiento deportivo: la mesa sigue abierta. Puedes intentarlo de nuevo, sin perder carta.")
					}),
				duel("queen_deling", "card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas", 6, 7,
					Options.AIType.OFFENSIVE, Ruleset.ELEMENT, 95, true,
					"Viajante da mesa", "Table traveler", "Viajera de la mesa",
					"Chefe de Deling. Elemental alto e jogo curto.", "Deling boss. High Elemental and a short game.", "Jefa de Deling. Elemental alto y juego corto.",
					new Line[] {
					N("A Rainha das Cartas, agora em Deling. Chefe do grupo da capital.",
						"The Queen of Cards, now in Deling. Boss of the capital group.",
						"La Reina de las Cartas, ahora en Deling. Jefa del grupo de la capital."),
					N("Same, Plus, Combo e Elemental. Se {name} vencer: Alexander, carta fixa, e mais 1 carta do deck dela.",
						"Same, Plus, Combo and Elemental. If {name} wins: Alexander, the fixed card, plus 1 more from her deck.",
						"Same, Plus, Combo y Elemental. Si {name} gana: Alexander, carta fija, y 1 carta más de su mazo.")
					},
					new Line[] {
					N("Grupo de Deling, mesa 3 de 3, chefe da etapa! A Rainha das Cartas, na capital, contra {name}!",
						"Deling Group, table 3 of 3, stage boss! The Queen of Cards, in the capital, against {name}!",
						"¡Grupo de Deling, mesa 3 de 3, jefa de la etapa! ¡La Reina de las Cartas, en la capital, contra {name}!"),
					N("Regras: Same, Plus, Combo e Elemental. Chefe: Alexander como carta fixa, mais 1 carta do deck da Rainha. Cortam os 2 primeiros.",
						"Rules: Same, Plus, Combo and Elemental. Boss: Alexander as the fixed card, plus 1 card from the Queen's deck. Top 2 go through.",
						"Reglas: Same, Plus, Combo y Elemental. Jefa: Alexander como carta fija, más 1 carta del mazo de la Reina. Pasan los 2 primeros.")
					},
					say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"A capital não perdoa um -1. Feche o chão.",
						"The capital doesn't forgive a -1. Close the floor.",
						"La capital no perdona un -1. Cierra el suelo."),
					lost("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"Mesa fechada. Leve Alexander e escolha mais uma.",
						"Table closed. Take Alexander and pick one more.",
						"Mesa cerrada. Llévate a Alexander y elige una más."),
					say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"A capital ficou comigo. A tabela espera a sua revanche.",
						"The capital stayed with me. The standings will wait for your rematch.",
						"La capital se quedó conmigo. La tabla espera tu revancha."),
					new Line[] {
					N("Vitória de {name}! {O campeão|A campeã} da mesa leva a carta fixa deste chefe e ainda escolhe mais uma no deck adversário.",
						"Win for {name}! The table winner takes this boss's fixed card and also picks one more from the opponent's deck.",
						"¡Victoria de {name}! {El campeón|La campeona} de la mesa se lleva la carta fija de este jefe y elige una más del mazo rival.")
					},
					new Line[] {
					N("Derrota de {name}. Reconhecimento esportivo: a mesa fica aberta. Pode tentar de novo, sem perder carta.",
						"Defeat for {name}. A sportsmanlike reading: the table stays open. You may try again, and you lose no card.",
						"Derrota de {name}. Reconocimiento deportivo: la mesa sigue abierta. Puedes intentarlo de nuevo, sin perder carta.")
					})
			});
	}

	private static Region dollet() {
		return region("dollet", "chegada_dollet", "mesa_dollet.png", Kind.GROUP, 2,
			new String[][] {{"queen_dollet", "pub"}, {"queen_dollet", "soldier_dollet"}, {"pub", "soldier_dollet"}},
			"Dollet - Rodada decisiva", "Dollet - Decisive round", "Dollet - Ronda decisiva",
			new Line[] {
				N("Deling mandou os 2 primeiros. {name} está entre eles. Dollet sedia a rodada decisiva. Cortam os 2 primeiros.",
					"Deling sent the top 2. {name} is one of them. Dollet hosts the decisive round. Top 2 go through.",
					"Deling mandó a los 2 primeros. {name} está entre ellos. Dollet acoge la ronda decisiva. Pasan los 2 primeros."),
				Q("A mesa do pub é curta. Elemental ainda vale. Não erre o chão.",
					"The pub table is short. Elemental still counts. Don't miss the floor.",
					"La mesa del pub es corta. Elemental sigue valiendo. No falles el suelo.")
			},
			new Duel[] {
				duel("pub", "npc_pub_dollet", "Tomas Reed", "Tomas Reed", "Tomas Reed", 5, 6,
					Options.AIType.DEFENSIVE, Ruleset.PLUS, 0, true,
					"Dono de pub em Dollet", "Dollet pub owner", "Dueño de pub en Dollet",
					"Plus de balcão. Jogo lento e defensivo.", "Bar Plus. Slow, defensive play.", "Plus de barra. Juego lento y defensivo.",
					new Line[] {
					N("Tomas Reed, dono de pub em Dollet. Primeira mesa do grupo da vila.",
						"Tomas Reed, a pub owner in Dollet. First table of the village group.",
						"Tomas Reed, dueño de pub en Dollet. Primera mesa del grupo del pueblo."),
					N("Same, Plus e Combo. Recompensa: 1 carta do deck dele. Cortam os 2 primeiros.",
						"Same, Plus and Combo. Prize: 1 card from his deck. Top 2 go through.",
						"Same, Plus y Combo. Recompensa: 1 carta de su mazo. Pasan los 2 primeros.")
					},
					new Line[] {
					N("Grupo de Dollet, mesa 1 de 3! Tomas Reed, dono de pub em Dollet, contra {name}!",
						"Dollet Group, table 1 of 3! Tomas Reed, pub owner in Dollet, against {name}!",
						"¡Grupo de Dollet, mesa 1 de 3! ¡Tomas Reed, dueño de pub en Dollet, contra {name}!"),
					N("Regras: Same, Plus e Combo. Em jogo: 1 carta do deck de Tomas. Vitória vale 3 pontos. Cortam os 2 primeiros.",
						"Rules: Same, Plus and Combo. At stake: 1 card from Tomas's deck. A win is worth 3 points. Top 2 go through.",
						"Reglas: Same, Plus y Combo. En juego: 1 carta del mazo de Tomas. La victoria vale 3 puntos. Pasan los 2 primeros.")
					},
					say("npc_pub_dollet", "Tomas Reed", "Tomas Reed", "Tomas Reed",
						"Aqui o Plus sai devagar, como o copo. Some com calma.",
						"Plus comes slow here, like the drink. Add it up calmly.",
						"Aquí el Plus sale despacio, como el vaso. Suma con calma."),
					lost("npc_pub_dollet", "Tomas Reed", "Tomas Reed", "Tomas Reed",
						"Boa soma. A carta sai do balcão para você.",
						"Good sum. The card leaves the bar for you.",
						"Buena suma. La carta sale de la barra hacia ti."),
					say("npc_pub_dollet", "Tomas Reed", "Tomas Reed", "Tomas Reed",
						"O copo ficou comigo. Quando quiser, a mesa reabre.",
						"The glass stayed with me. When you like, the table opens again.",
						"El vaso se quedó conmigo. Cuando quieras, la mesa se reabre."),
					new Line[] {
					N("Vitória de {name}! Escolha 1 carta do deck que você acabou de enfrentar. A derrota não teria tirado nada.",
						"Win for {name}! Choose 1 card from the deck you just faced. A loss would have taken nothing.",
						"¡Victoria de {name}! Elige 1 carta del mazo que acabas de enfrentar. La derrota no habría quitado nada.")
					},
					new Line[] {
					N("Derrota de {name}. Reconhecimento esportivo: a mesa fica aberta. Pode tentar de novo, sem perder carta.",
						"Defeat for {name}. A sportsmanlike reading: the table stays open. You may try again, and you lose no card.",
						"Derrota de {name}. Reconocimiento deportivo: la mesa sigue abierta. Puedes intentarlo de nuevo, sin perder carta.")
					}),
				duel("soldier_dollet", "npc_soldado_dollet", "Enne Doss", "Enne Doss", "Enne Doss", 6, 7,
					Options.AIType.BALANCED, Ruleset.ELEMENT, 0, true,
					"Guarda de Dollet", "Dollet guard", "Guardia de Dollet",
					"Elemental da vila. Ele cobre os lados da praça.", "Village Elemental. He covers the square's sides.", "Elemental del pueblo. Cubre los lados de la plaza.",
					new Line[] {
					N("Enne Doss, guarda de Dollet. Segunda mesa do grupo.",
						"Enne Doss, a Dollet guard. Second table of the group.",
						"Enne Doss, guardia de Dollet. Segunda mesa del grupo."),
					N("Same, Plus, Combo e Elemental. Recompensa: 1 carta do deck dele.",
						"Same, Plus, Combo and Elemental. Prize: 1 card from his deck.",
						"Same, Plus, Combo y Elemental. Recompensa: 1 carta de su mazo.")
					},
					new Line[] {
					N("Grupo de Dollet, mesa 2 de 3! Enne Doss, guarda de Dollet, contra {name}!",
						"Dollet Group, table 2 of 3! Enne Doss, Dollet guard, against {name}!",
						"¡Grupo de Dollet, mesa 2 de 3! ¡Enne Doss, guardia de Dollet, contra {name}!"),
					N("Regras: Same, Plus, Combo e Elemental. Em jogo: 1 carta do deck de Enne.",
						"Rules: Same, Plus, Combo and Elemental. At stake: 1 card from Enne's deck.",
						"Reglas: Same, Plus, Combo y Elemental. En juego: 1 carta del mazo de Enne.")
					},
					say("npc_soldado_dollet", "Enne Doss", "Enne Doss", "Enne Doss",
						"Eu cubro a praça. O chão desta vila também joga.",
						"I cover the square. This village floor also plays.",
						"Cubro la plaza. El suelo de este pueblo también juega."),
					lost("npc_soldado_dollet", "Enne Doss", "Enne Doss", "Enne Doss",
						"A praça ficou com você. Leve a carta.",
						"The square stayed with you. Take the card.",
						"La plaza se quedó contigo. Llévate la carta."),
					say("npc_soldado_dollet", "Enne Doss", "Enne Doss", "Enne Doss",
						"A praça ficou comigo. Volte ao posto quando quiser.",
						"The square stayed with me. Return to the post when you like.",
						"La plaza se quedó conmigo. Vuelve al puesto cuando quieras."),
					new Line[] {
					N("Vitória de {name}! Escolha 1 carta do deck que você acabou de enfrentar. A derrota não teria tirado nada.",
						"Win for {name}! Choose 1 card from the deck you just faced. A loss would have taken nothing.",
						"¡Victoria de {name}! Elige 1 carta del mazo que acabas de enfrentar. La derrota no habría quitado nada.")
					},
					new Line[] {
					N("Derrota de {name}. Reconhecimento esportivo: a mesa fica aberta. Pode tentar de novo, sem perder carta.",
						"Defeat for {name}. A sportsmanlike reading: the table stays open. You may try again, and you lose no card.",
						"Derrota de {name}. Reconocimiento deportivo: la mesa sigue abierta. Puedes intentarlo de nuevo, sin perder carta.")
					}),
				duel("queen_dollet", "card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas", 6, 8,
					Options.AIType.OFFENSIVE, Ruleset.ELEMENT, 90, true,
					"Viajante da mesa", "Table traveler", "Viajera de la mesa",
					"Chefe de Dollet. Elemental alto, pouco espaço para erro.", "Dollet boss. High Elemental, little room for error.", "Jefa de Dollet. Elemental alto, poco margen de error.",
					new Line[] {
					N("A Rainha das Cartas, agora em Dollet. Chefe do grupo da vila.",
						"The Queen of Cards, now in Dollet. Boss of the village group.",
						"La Reina de las Cartas, ahora en Dollet. Jefa del grupo del pueblo."),
					N("Same, Plus, Combo e Elemental. Se {name} vencer: Diablos, carta fixa, e mais 1 carta do deck dela.",
						"Same, Plus, Combo and Elemental. If {name} wins: Diablos, the fixed card, plus 1 more from her deck.",
						"Same, Plus, Combo y Elemental. Si {name} gana: Diablos, carta fija, y 1 carta más de su mazo.")
					},
					new Line[] {
					N("Grupo de Dollet, mesa 3 de 3, chefe da etapa! A Rainha das Cartas, na vila, contra {name}!",
						"Dollet Group, table 3 of 3, stage boss! The Queen of Cards, in the village, against {name}!",
						"¡Grupo de Dollet, mesa 3 de 3, jefa de la etapa! ¡La Reina de las Cartas, en el pueblo, contra {name}!"),
					N("Regras: Same, Plus, Combo e Elemental. Chefe: Diablos como carta fixa, mais 1 carta do deck da Rainha. Cortam os 2 primeiros.",
						"Rules: Same, Plus, Combo and Elemental. Boss: Diablos as the fixed card, plus 1 card from the Queen's deck. Top 2 go through.",
						"Reglas: Same, Plus, Combo y Elemental. Jefa: Diablos como carta fija, más 1 carta del mazo de la Reina. Pasan los 2 primeros.")
					},
					say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"A vila é pequena e a mesa é cheia. Não erre o chão.",
						"The village is small and the table is full. Don't miss the floor.",
						"El pueblo es pequeño y la mesa está llena. No falles el suelo."),
					lost("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"Mesa da vila fechada. Leve Diablos e escolha mais uma.",
						"Village table closed. Take Diablos and pick one more.",
						"Mesa del pueblo cerrada. Llévate a Diablos y elige una más."),
					say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"A vila ficou comigo. A revanche está na mesma cadeira.",
						"The village stayed with me. The rematch is in the same chair.",
						"El pueblo se quedó conmigo. La revancha está en la misma silla."),
					new Line[] {
					N("Vitória de {name}! {O campeão|A campeã} da mesa leva a carta fixa deste chefe e ainda escolhe mais uma no deck adversário.",
						"Win for {name}! The table winner takes this boss's fixed card and also picks one more from the opponent's deck.",
						"¡Victoria de {name}! {El campeón|La campeona} de la mesa se lleva la carta fija de este jefe y elige una más del mazo rival.")
					},
					new Line[] {
					N("Derrota de {name}. Reconhecimento esportivo: a mesa fica aberta. Pode tentar de novo, sem perder carta.",
						"Defeat for {name}. A sportsmanlike reading: the table stays open. You may try again, and you lose no card.",
						"Derrota de {name}. Reconocimiento deportivo: la mesa sigue abierta. Puedes intentarlo de nuevo, sin perder carta.")
					})
			});
	}

	private static Region fh() {
		return region("fh", "chegada_fh", "mesa_fh.png", Kind.GROUP, 1,
			new String[][] {{"flo", "pescador"}, {"flo", "shumi"}, {"shumi", "pescador"}},
			"FH - Quartas", "FH - Quarterfinals", "FH - Cuartos",
			new Line[] {
				N("Dollet fechou o corte. {name} entrou nas quartas em Fisherman's Horizon. Só o primeiro corta. A mesa contra a Rainha é de chave: não vale ponto e não elimina.",
					"Dollet set the cutoff. {name} is in the FH quarterfinal. Only 1st goes through. The match against the Queen is a seeding table: it scores nothing and eliminates no one.",
					"Dollet cerró el corte. {name} entró en los cuartos de Fisherman's Horizon. Solo pasa el primero. La mesa contra la Reina es de clave: no da puntos y no elimina."),
				Q("Aqui entram Same Wall e Sudden Death, na mesa de chave. A parede conta como A. Empate 5 a 5 recomeça a mesa.",
					"Same Wall and Sudden Death arrive here, at the seeding table. The wall counts as A. A 5-5 tie starts the table again.",
					"Aquí entran Same Wall y Sudden Death, en la mesa de clave. La pared cuenta como A. Un empate 5 a 5 reempieza la mesa.")
			},
			new Duel[] {
				duel("queen_fh", "card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas", 6, 7,
					Options.AIType.OFFENSIVE, Ruleset.FINAL, 0, false,
					"Viajante da mesa", "Table traveler", "Viajera de la mesa",
					"Mesa de chave: Same Wall e Sudden Death entram aqui.", "Seeding table: Same Wall and Sudden Death arrive here.", "Mesa de clave: Same Wall y Sudden Death entran aquí.",
					new Line[] {
					N("A Rainha das Cartas, em Fisherman's Horizon. Mesa de chave, sem pontos e sem eliminação.",
						"The Queen of Cards, in Fisherman's Horizon. A seeding table, no points and no elimination.",
						"La Reina de las Cartas, en Fisherman's Horizon. Mesa de clave, sin puntos y sin eliminación."),
					N("Same, Same Wall, Plus, Combo, Elemental e Sudden Death. Recompensa: 1 carta do deck dela.",
						"Same, Same Wall, Plus, Combo, Elemental and Sudden Death. Prize: 1 card from her deck.",
						"Same, Same Wall, Plus, Combo, Elemental y Sudden Death. Recompensa: 1 carta de su mazo.")
					},
					new Line[] {
					N("Fisherman's Horizon, mesa de chave! A Rainha das Cartas contra {name}!",
						"Fisherman's Horizon, seeding table! The Queen of Cards against {name}!",
						"¡Fisherman's Horizon, mesa de clave! ¡La Reina de las Cartas contra {name}!"),
					N("Regras: Same, Same Wall, Plus, Combo, Elemental e Sudden Death. Não vale ponto e não elimina. Em jogo: 1 carta do deck da Rainha.",
						"Rules: Same, Same Wall, Plus, Combo, Elemental and Sudden Death. It scores nothing and eliminates no one. At stake: 1 card from the Queen's deck.",
						"Reglas: Same, Same Wall, Plus, Combo, Elemental y Sudden Death. No da puntos y no elimina. En juego: 1 carta del mazo de la Reina.")
					},
					say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"A parede conta como A. Sudden Death reabre a mesa se empatar 5 a 5.",
						"The wall counts as A. Sudden Death reopens the table if it ties 5 to 5.",
						"La pared cuenta como A. Sudden Death reabre la mesa si empata 5 a 5."),
					lost("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"Chave marcada. Escolha a carta. O grupo de FH continua.",
						"Seed marked. Pick the card. The FH group goes on.",
						"Clave marcada. Elige la carta. El grupo de FH sigue."),
					say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"A chave ficou comigo, mas você não sai do grupo. Sente de novo se quiser a carta.",
						"The seed stayed with me, but you don't leave the group. Sit again if you want the card.",
						"La clave se quedó conmigo, pero no sales del grupo. Siéntate de nuevo si quieres la carta."),
					new Line[] {
					N("Vitória de {name}! Escolha 1 carta do deck que você acabou de enfrentar. A derrota não teria tirado nada.",
						"Win for {name}! Choose 1 card from the deck you just faced. A loss would have taken nothing.",
						"¡Victoria de {name}! Elige 1 carta del mazo que acabas de enfrentar. La derrota no habría quitado nada."),
					Q("Same Wall: a borda vale A. Sudden Death: empate 5 a 5 recomeça com as cartas da cor de cada um.",
						"Same Wall: the edge counts as A. Sudden Death: a 5-5 tie starts again with each side's color.",
						"Same Wall: el borde vale A. Sudden Death: un empate 5 a 5 reempieza con las cartas del color de cada uno.")
					},
					new Line[] {
					N("Derrota de {name}. Reconhecimento esportivo: a mesa fica aberta. Pode tentar de novo, sem perder carta.",
						"Defeat for {name}. A sportsmanlike reading: the table stays open. You may try again, and you lose no card.",
						"Derrota de {name}. Reconocimiento deportivo: la mesa sigue abierta. Puedes intentarlo de nuevo, sin perder carta.")
					}),
				duel("pescador", "npc_pescador", "Ryn Kess", "Ryn Kess", "Ryn Kess", 5, 6,
					Options.AIType.BALANCED, Ruleset.PLUS, 0, true,
					"Engenheiro de FH", "FH engineer", "Ingeniero de FH",
					"Plus da oficina. Ele mede antes de soltar.", "Workshop Plus. He measures before he drops.", "Plus del taller. Mide antes de soltar.",
					new Line[] {
					N("Ryn Kess, engenheiro de Fisherman's Horizon. Primeira mesa do grupo da oficina.",
						"Ryn Kess, an engineer in Fisherman's Horizon. First table of the workshop group.",
						"Ryn Kess, ingeniero de Fisherman's Horizon. Primera mesa del grupo del taller."),
					N("Same, Plus e Combo. Recompensa: 1 carta do deck dele. Em FH, só o primeiro corta.",
						"Same, Plus and Combo. Prize: 1 card from his deck. At FH, only 1st goes through.",
						"Same, Plus y Combo. Recompensa: 1 carta de su mazo. En FH, solo pasa el primero.")
					},
					new Line[] {
					N("Grupo de FH, mesa 1 de 3! Ryn Kess, engenheiro de Fisherman's Horizon, contra {name}!",
						"FH Group, table 1 of 3! Ryn Kess, Fisherman's Horizon engineer, against {name}!",
						"¡Grupo de FH, mesa 1 de 3! ¡Ryn Kess, ingeniero de Fisherman's Horizon, contra {name}!"),
					N("Regras: Same, Plus e Combo. Em jogo: 1 carta do deck de Ryn. Vitória vale 3 pontos. Só o primeiro corta.",
						"Rules: Same, Plus and Combo. At stake: 1 card from Ryn's deck. A win is worth 3 points. Only 1st goes through.",
						"Reglas: Same, Plus y Combo. En juego: 1 carta del mazo de Ryn. La victoria vale 3 puntos. Solo pasa el primero.")
					},
					say("npc_pescador", "Ryn Kess", "Ryn Kess", "Ryn Kess",
						"Eu meço a soma duas vezes. Faça o mesmo.",
						"I measure the sum twice. Do the same.",
						"Mido la suma dos veces. Haz lo mismo."),
					lost("npc_pescador", "Ryn Kess", "Ryn Kess", "Ryn Kess",
						"A medida fechou do seu lado. Leve a carta.",
						"The measure closed on your side. Take the card.",
						"La medida cerró de tu lado. Llévate la carta."),
					say("npc_pescador", "Ryn Kess", "Ryn Kess", "Ryn Kess",
						"A medida ficou comigo. Recalcule e volte.",
						"The measure stayed with me. Recalculate and come back.",
						"La medida se quedó conmigo. Recalcula y vuelve."),
					new Line[] {
					N("Vitória de {name}! Escolha 1 carta do deck que você acabou de enfrentar. A derrota não teria tirado nada.",
						"Win for {name}! Choose 1 card from the deck you just faced. A loss would have taken nothing.",
						"¡Victoria de {name}! Elige 1 carta del mazo que acabas de enfrentar. La derrota no habría quitado nada.")
					},
					new Line[] {
					N("Derrota de {name}. Reconhecimento esportivo: a mesa fica aberta. Pode tentar de novo, sem perder carta.",
						"Defeat for {name}. A sportsmanlike reading: the table stays open. You may try again, and you lose no card.",
						"Derrota de {name}. Reconocimiento deportivo: la mesa sigue abierta. Puedes intentarlo de nuevo, sin perder carta.")
					}),
				duel("shumi", "shumi", "Ancião Muum", "Elder Muum", "Anciano Muum", 6, 7,
					Options.AIType.BALANCED, Ruleset.ELEMENT, 0, true,
					"Ancião Shumi", "Shumi elder", "Anciano Shumi",
					"Elemental calmo. Ele espera o chão trabalhar.", "Calm Elemental. He waits for the floor to work.", "Elemental calmo. Espera a que el suelo trabaje.",
					new Line[] {
					N("Ancião Muum, do povo Shumi em FH. Segunda mesa do grupo.",
						"Elder Muum, of the Shumi people in FH. Second table of the group.",
						"Anciano Muum, del pueblo Shumi en FH. Segunda mesa del grupo."),
					N("Same, Plus, Combo e Elemental. Recompensa: 1 carta do deck dele.",
						"Same, Plus, Combo and Elemental. Prize: 1 card from his deck.",
						"Same, Plus, Combo y Elemental. Recompensa: 1 carta de su mazo.")
					},
					new Line[] {
					N("Grupo de FH, mesa 2 de 3! Ancião Muum, do povo Shumi, contra {name}!",
						"FH Group, table 2 of 3! Elder Muum, of the Shumi people, against {name}!",
						"¡Grupo de FH, mesa 2 de 3! ¡Anciano Muum, del pueblo Shumi, contra {name}!"),
					N("Regras: Same, Plus, Combo e Elemental. Em jogo: 1 carta do deck do Ancião Muum.",
						"Rules: Same, Plus, Combo and Elemental. At stake: 1 card from Elder Muum's deck.",
						"Reglas: Same, Plus, Combo y Elemental. En juego: 1 carta del mazo del Anciano Muum.")
					},
					say("shumi", "Ancião Muum", "Elder Muum", "Anciano Muum",
						"O chão desta oficina também fala. Escute antes de soltar.",
						"The floor of this workshop also speaks. Listen before you drop.",
						"El suelo de este taller también habla. Escucha antes de soltar."),
					lost("shumi", "Ancião Muum", "Elder Muum", "Anciano Muum",
						"Você escutou o chão. A carta segue com {name}.",
						"You listened to the floor. The card goes with {name}.",
						"Escuchaste el suelo. La carta sigue con {name}."),
					say("shumi", "Ancião Muum", "Elder Muum", "Anciano Muum",
						"O chão não foi ouvido. Volte e escute de novo.",
						"The floor was not heard. Come back and listen again.",
						"El suelo no fue escuchado. Vuelve y escucha de nuevo."),
					new Line[] {
					N("Vitória de {name}! Escolha 1 carta do deck que você acabou de enfrentar. A derrota não teria tirado nada.",
						"Win for {name}! Choose 1 card from the deck you just faced. A loss would have taken nothing.",
						"¡Victoria de {name}! Elige 1 carta del mazo que acabas de enfrentar. La derrota no habría quitado nada.")
					},
					new Line[] {
					N("Derrota de {name}. Reconhecimento esportivo: a mesa fica aberta. Pode tentar de novo, sem perder carta.",
						"Defeat for {name}. A sportsmanlike reading: the table stays open. You may try again, and you lose no card.",
						"Derrota de {name}. Reconocimiento deportivo: la mesa sigue abierta. Puedes intentarlo de nuevo, sin perder carta.")
					}),
				duel("flo", "dobe_flo", "Dobe e Flo", "Dobe and Flo", "Dobe y Flo", 7, 8,
					Options.AIType.OFFENSIVE, Ruleset.ELEMENT, 96, true,
					"Moradores de FH", "FH residents", "Vecinos de FH",
					"Chefes de FH. Elemental alto na oficina.", "FH bosses. High Elemental in the workshop.", "Jefes de FH. Elemental alto en el taller.",
					new Line[] {
					N("Dobe e Flo, moradores de Fisherman's Horizon. Chefes do grupo da oficina.",
						"Dobe and Flo, residents of Fisherman's Horizon. Bosses of the workshop group.",
						"Dobe y Flo, vecinos de Fisherman's Horizon. Jefes del grupo del taller."),
					N("Same, Plus, Combo e Elemental. Se {name} vencer: Phoenix, carta fixa, e mais 1 carta do deck deles.",
						"Same, Plus, Combo and Elemental. If {name} wins: Phoenix, the fixed card, plus 1 more from their deck.",
						"Same, Plus, Combo y Elemental. Si {name} gana: Phoenix, carta fija, y 1 carta más de su mazo.")
					},
					new Line[] {
					N("Grupo de FH, mesa 3 de 3, chefes da etapa! Dobe e Flo, da oficina, contra {name}!",
						"FH Group, table 3 of 3, stage bosses! Dobe and Flo, from the workshop, against {name}!",
						"¡Grupo de FH, mesa 3 de 3, jefes de la etapa! ¡Dobe y Flo, del taller, contra {name}!"),
					N("Regras: Same, Plus, Combo e Elemental. Chefes: Phoenix como carta fixa, mais 1 carta do deck de Dobe e Flo. Só o primeiro corta.",
						"Rules: Same, Plus, Combo and Elemental. Bosses: Phoenix as the fixed card, plus 1 card from Dobe and Flo's deck. Only 1st goes through.",
						"Reglas: Same, Plus, Combo y Elemental. Jefes: Phoenix como carta fija, más 1 carta del mazo de Dobe y Flo. Solo pasa el primero.")
					},
					say("dobe_flo", "Dobe e Flo", "Dobe and Flo", "Dobe y Flo",
						"A oficina é nossa. O chão também. Feche se puder.",
						"The workshop is ours. So is the floor. Close it if you can.",
						"El taller es nuestro. El suelo también. Ciérralo si puedes."),
					lost("dobe_flo", "Dobe e Flo", "Dobe and Flo", "Dobe y Flo",
						"Você fechou a oficina. Leve Phoenix e escolha mais uma.",
						"You closed the workshop. Take Phoenix and pick one more.",
						"Cerraste el taller. Llévate a Phoenix y elige una más."),
					say("dobe_flo", "Dobe e Flo", "Dobe and Flo", "Dobe y Flo",
						"A oficina ficou conosco. A cadeira segue aberta.",
						"The workshop stayed with us. The chair stays open.",
						"El taller se quedó con nosotros. La silla sigue abierta."),
					new Line[] {
					N("Vitória de {name}! {O campeão|A campeã} da mesa leva a carta fixa deste chefe e ainda escolhe mais uma no deck adversário.",
						"Win for {name}! The table winner takes this boss's fixed card and also picks one more from the opponent's deck.",
						"¡Victoria de {name}! {El campeón|La campeona} de la mesa se lleva la carta fija de este jefe y elige una más del mazo rival.")
					},
					new Line[] {
					N("Derrota de {name}. Reconhecimento esportivo: a mesa fica aberta. Pode tentar de novo, sem perder carta.",
						"Defeat for {name}. A sportsmanlike reading: the table stays open. You may try again, and you lose no card.",
						"Derrota de {name}. Reconocimiento deportivo: la mesa sigue abierta. Puedes intentarlo de nuevo, sin perder carta.")
					})
			});
	}

	private static Region esthar() {
		return region("esthar", "chegada_esthar", "mesa_esthar.png", Kind.GROUP, 1,
			new String[][] {{"seifer", "cientista"}, {"seifer", "odine"}, {"odine", "cientista"}},
			"Esthar - Semifinal", "Esthar - Semifinal", "Esthar - Semifinal",
			new Line[] {
				N("{name} terminou em primeiro nas quartas de FH. A semifinal deste lado é em Esthar. Só o primeiro corta.",
					"{name} finished first in the FH quarterfinal. This side's semifinal is in Esthar. Only 1st goes through.",
					"{name} terminó {primero|primera} en los cuartos de FH. La semifinal de este lado es en Esthar. Solo pasa el primero."),
				Q("Same Wall e Sudden Death voltam com Odine e Seifer. A parede ainda vale A.",
					"Same Wall and Sudden Death return with Odine and Seifer. The wall still counts as A.",
					"Same Wall y Sudden Death vuelven con Odine y Seifer. La pared sigue valiendo A.")
			},
			new Duel[] {
				duel("cientista", "npc_cientista_esthar", "Ivo Lenn", "Ivo Lenn", "Ivo Lenn", 6, 7,
					Options.AIType.OFFENSIVE, Ruleset.ELEMENT, 0, true,
					"Pesquisador de Esthar", "Esthar researcher", "Investigador de Esthar",
					"Elemental de laboratório. Ele testa o chão primeiro.", "Lab Elemental. He tests the floor first.", "Elemental de laboratorio. Prueba el suelo primero.",
					new Line[] {
					N("Ivo Lenn, pesquisador de Esthar. Primeira mesa da semifinal.",
						"Ivo Lenn, an Esthar researcher. First table of the semifinal.",
						"Ivo Lenn, investigador de Esthar. Primera mesa de la semifinal."),
					N("Same, Plus, Combo e Elemental. Recompensa: 1 carta do deck dele. Só o primeiro corta.",
						"Same, Plus, Combo and Elemental. Prize: 1 card from his deck. Only 1st goes through.",
						"Same, Plus, Combo y Elemental. Recompensa: 1 carta de su mazo. Solo pasa el primero.")
					},
					new Line[] {
					N("Semifinal de Esthar, mesa 1 de 3! Ivo Lenn, pesquisador de Esthar, contra {name}!",
						"Esthar semifinal, table 1 of 3! Ivo Lenn, Esthar researcher, against {name}!",
						"¡Semifinal de Esthar, mesa 1 de 3! ¡Ivo Lenn, investigador de Esthar, contra {name}!"),
					N("Regras: Same, Plus, Combo e Elemental. Em jogo: 1 carta do deck de Ivo. Vitória vale 3 pontos. Só o primeiro corta.",
						"Rules: Same, Plus, Combo and Elemental. At stake: 1 card from Ivo's deck. A win is worth 3 points. Only 1st goes through.",
						"Reglas: Same, Plus, Combo y Elemental. En juego: 1 carta del mazo de Ivo. La victoria vale 3 puntos. Solo pasa el primero.")
					},
					say("npc_cientista_esthar", "Ivo Lenn", "Ivo Lenn", "Ivo Lenn",
						"Primeiro o chão, depois a carta. É o método.",
						"The floor first, then the card. That's the method.",
						"Primero el suelo, después la carta. Es el método."),
					lost("npc_cientista_esthar", "Ivo Lenn", "Ivo Lenn", "Ivo Lenn",
						"O teste fechou do seu lado. Leve a carta.",
						"The test closed on your side. Take the card.",
						"La prueba cerró de tu lado. Llévate la carta."),
					say("npc_cientista_esthar", "Ivo Lenn", "Ivo Lenn", "Ivo Lenn",
						"O teste ficou comigo. Repita o método.",
						"The test stayed with me. Repeat the method.",
						"La prueba se quedó conmigo. Repite el método."),
					new Line[] {
					N("Vitória de {name}! Escolha 1 carta do deck que você acabou de enfrentar. A derrota não teria tirado nada.",
						"Win for {name}! Choose 1 card from the deck you just faced. A loss would have taken nothing.",
						"¡Victoria de {name}! Elige 1 carta del mazo que acabas de enfrentar. La derrota no habría quitado nada.")
					},
					new Line[] {
					N("Derrota de {name}. Reconhecimento esportivo: a mesa fica aberta. Pode tentar de novo, sem perder carta.",
						"Defeat for {name}. A sportsmanlike reading: the table stays open. You may try again, and you lose no card.",
						"Derrota de {name}. Reconocimiento deportivo: la mesa sigue abierta. Puedes intentarlo de nuevo, sin perder carta.")
					}),
				duel("odine", "odine", "Odine", "Odine", "Odine", 7, 8,
					Options.AIType.OFFENSIVE, Ruleset.FINAL, 0, true,
					"Cientista de Esthar", "Esthar scientist", "Científico de Esthar",
					"Same Wall e Sudden Death voltam, com cartas altas.", "Same Wall and Sudden Death return, with high cards.", "Same Wall y Sudden Death vuelven, con cartas altas.",
					new Line[] {
					N("Doutor Odine, de Esthar. Segunda mesa da semifinal.",
						"Doctor Odine, of Esthar. Second table of the semifinal.",
						"Doctor Odine, de Esthar. Segunda mesa de la semifinal."),
					N("Same, Same Wall, Plus, Combo, Elemental e Sudden Death. Recompensa: 1 carta do deck dele.",
						"Same, Same Wall, Plus, Combo, Elemental and Sudden Death. Prize: 1 card from his deck.",
						"Same, Same Wall, Plus, Combo, Elemental y Sudden Death. Recompensa: 1 carta de su mazo.")
					},
					new Line[] {
					N("Semifinal de Esthar, mesa 2 de 3! Doutor Odine, de Esthar, contra {name}!",
						"Esthar semifinal, table 2 of 3! Doctor Odine, of Esthar, against {name}!",
						"¡Semifinal de Esthar, mesa 2 de 3! ¡Doctor Odine, de Esthar, contra {name}!"),
					N("Regras: Same, Same Wall, Plus, Combo, Elemental e Sudden Death. Em jogo: 1 carta do deck de Odine. A parede vale A.",
						"Rules: Same, Same Wall, Plus, Combo, Elemental and Sudden Death. At stake: 1 card from Odine's deck. The wall counts as A.",
						"Reglas: Same, Same Wall, Plus, Combo, Elemental y Sudden Death. En juego: 1 carta del mazo de Odine. La pared vale A.")
					},
					say("odine", "Odine", "Odine", "Odine",
						"A parede é A, sim. Use-a, ou eu uso.",
						"The wall is A, yes. Use it, or I will.",
						"La pared es A, sí. Úsala, o la uso yo."),
					lost("odine", "Odine", "Odine", "Odine",
						"A parede ficou com você. Leve a carta.",
						"The wall stayed with you. Take the card.",
						"La pared se quedó contigo. Llévate la carta."),
					say("odine", "Odine", "Odine", "Odine",
						"Eu usei a parede. Volte com outra conta.",
						"I used the wall. Come back with another count.",
						"Yo usé la pared. Vuelve con otra cuenta."),
					new Line[] {
					N("Vitória de {name}! Escolha 1 carta do deck que você acabou de enfrentar. A derrota não teria tirado nada.",
						"Win for {name}! Choose 1 card from the deck you just faced. A loss would have taken nothing.",
						"¡Victoria de {name}! Elige 1 carta del mazo que acabas de enfrentar. La derrota no habría quitado nada.")
					},
					new Line[] {
					N("Derrota de {name}. Reconhecimento esportivo: a mesa fica aberta. Pode tentar de novo, sem perder carta.",
						"Defeat for {name}. A sportsmanlike reading: the table stays open. You may try again, and you lose no card.",
						"Derrota de {name}. Reconocimiento deportivo: la mesa sigue abierta. Puedes intentarlo de nuevo, sin perder carta.")
					}),
				duel("seifer", "seifer", "Seifer", "Seifer", "Seifer", 8, 9,
					Options.AIType.OFFENSIVE, Ruleset.FINAL, 109, true,
					"Candidato da outra chave", "Other-ladder candidate", "Candidato de la otra llave",
					"Chefe de Esthar. Regras completas e cartas raras.", "Esthar boss. Full rules and rare cards.", "Jefe de Esthar. Reglas completas y cartas raras.",
					new Line[] {
					N("Seifer, da outra chave do Garden. Chefe da semifinal de Esthar.",
						"Seifer, from the Garden's other ladder. Boss of the Esthar semifinal.",
						"Seifer, de la otra llave del Garden. Jefe de la semifinal de Esthar."),
					N("Regras completas. Se {name} vencer: a carta Seifer, fixa, e mais 1 carta do deck dele.",
						"Full rules. If {name} wins: the Seifer card, fixed, plus 1 more from his deck.",
						"Reglas completas. Si {name} gana: la carta Seifer, fija, y 1 carta más de su mazo.")
					},
					new Line[] {
					N("Semifinal de Esthar, mesa 3 de 3, chefe da etapa! Seifer, da outra chave, contra {name}!",
						"Esthar semifinal, table 3 of 3, stage boss! Seifer, from the other ladder, against {name}!",
						"¡Semifinal de Esthar, mesa 3 de 3, jefe de la etapa! ¡Seifer, de la otra llave, contra {name}!"),
					N("Regras: Same, Same Wall, Plus, Combo, Elemental e Sudden Death. Chefe: a carta Seifer, fixa, mais 1 carta do deck dele. Só o primeiro corta. A final espera no Garden.",
						"Rules: Same, Same Wall, Plus, Combo, Elemental and Sudden Death. Boss: the Seifer card, fixed, plus 1 card from his deck. Only 1st goes through. The final waits at the Garden.",
						"Reglas: Same, Same Wall, Plus, Combo, Elemental y Sudden Death. Jefe: la carta Seifer, fija, más 1 carta de su mazo. Solo pasa el primero. La final espera en el Garden.")
					},
					say("seifer", "Seifer", "Seifer", "Seifer",
						"Eu não vou segurar. Mesma parede, mesmo Combo. Aguente.",
						"I won't hold back. Same wall, same Combo. Hold it.",
						"No me voy a contener. Misma pared, mismo Combo. Aguanta."),
					lost("seifer", "Seifer", "Seifer", "Seifer",
						"Você fechou a semifinal. Leve a minha carta e escolha mais uma.",
						"You closed the semifinal. Take my card and pick one more.",
						"Cerraste la semifinal. Llévate mi carta y elige una más."),
					say("seifer", "Seifer", "Seifer", "Seifer",
						"A chave ficou comigo. A cadeira não fecha. Volte.",
						"The ladder stayed with me. The chair doesn't close. Come back.",
						"La llave se quedó conmigo. La silla no se cierra. Vuelve."),
					new Line[] {
					N("Vitória de {name}! {O campeão|A campeã} da mesa leva a carta fixa deste chefe e ainda escolhe mais uma no deck adversário.",
						"Win for {name}! The table winner takes this boss's fixed card and also picks one more from the opponent's deck.",
						"¡Victoria de {name}! {El campeón|La campeona} de la mesa se lleva la carta fija de este jefe y elige una más del mazo rival."),
					Q("A final é uma mesa no Garden. Derrota dá revanche. O título só muda com a vitória.",
						"The final is one table at the Garden. A loss means a rematch. The title only moves with a win.",
						"La final es una mesa en el Garden. La derrota da revancha. El título solo cambia con la victoria.")
					},
					new Line[] {
					N("Derrota de {name}. Reconhecimento esportivo: a mesa fica aberta. Pode tentar de novo, sem perder carta.",
						"Defeat for {name}. A sportsmanlike reading: the table stays open. You may try again, and you lose no card.",
						"Derrota de {name}. Reconocimiento deportivo: la mesa sigue abierta. Puedes intentarlo de nuevo, sin perder carta.")
					})
			});
	}

	private static Region fin() {
		return region("final", "final_historia", StoryCampaign.FINAL_ARENA, Kind.FINAL, 0, null,
			"Garden - Grande final", "Garden - Grand Final", "Garden - Gran final",
			new Line[] {
				N("O Garden sedia a grande final porque a inscrição começou neste corredor. Uma mesa só. O título só muda com a vitória.",
					"The Garden hosts the grand final because registration started in this hall. One table only. The title only moves with a win.",
					"El Garden acoge la gran final porque la inscripción empezó en este pasillo. Una sola mesa. El título solo cambia con la victoria."),
				Q("Regras completas: Same Wall e Sudden Death entram de novo. Derrota dá revanche na hora.",
					"Full rules: Same Wall and Sudden Death are on again. A loss means an instant rematch.",
					"Reglas completas: Same Wall y Sudden Death entran de nuevo. La derrota da revancha al momento.")
			},
			new Duel[] {
				duel("queen_esthar", "card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas", 8, 10,
					Options.AIType.OFFENSIVE, Ruleset.FINAL, 97, false,
					"Campeã mundial", "World champion", "Campeona mundial",
					"Grande final. Regras completas, o teto do baralho.", "Grand final. Full rules, the top of the deck.", "Gran final. Reglas completas, el techo del mazo.",
					new Line[] {
					N("A Rainha das Cartas, na grande final do Garden. Chefe final do torneio.",
						"The Queen of Cards, in the Garden grand final. Final boss of the tournament.",
						"La Reina de las Cartas, en la gran final del Garden. Jefa final del torneo."),
					N("Regras completas. Se {name} vencer: Bahamut, carta fixa, e mais 1 carta do deck dela. O título só muda com esta vitória.",
						"Full rules. If {name} wins: Bahamut, the fixed card, plus 1 more from her deck. The title only moves with this win.",
						"Reglas completas. Si {name} gana: Bahamut, carta fija, y 1 carta más de su mazo. El título solo cambia con esta victoria.")
					},
					new Line[] {
					N("Grande final no Garden! A Rainha das Cartas, campeã mundial, contra {name}!",
						"Grand final at the Garden! The Queen of Cards, world champion, against {name}!",
						"¡Gran final en el Garden! ¡La Reina de las Cartas, campeona mundial, contra {name}!"),
					N("Regras: Same, Same Wall, Plus, Combo, Elemental e Sudden Death. Chefe final: Bahamut como carta fixa, mais 1 carta do deck da Rainha. Derrota dá revanche na hora.",
						"Rules: Same, Same Wall, Plus, Combo, Elemental and Sudden Death. Final boss: Bahamut as the fixed card, plus 1 card from the Queen's deck. A loss means an instant rematch.",
						"Reglas: Same, Same Wall, Plus, Combo, Elemental y Sudden Death. Jefa final: Bahamut como carta fija, más 1 carta del mazo de la Reina. La derrota da revancha al momento.")
					},
					say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"Esta é a última mesa. Eu jogo no teto do baralho. Feche se puder.",
						"This is the last table. I play at the top of the deck. Close it if you can.",
						"Esta es la última mesa. Juego en el techo del mazo. Ciérrala si puedes."),
					lost("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"O título passa. Leve Bahamut e escolha a última carta desta campanha.",
						"The title passes. Take Bahamut and pick the last card of this campaign.",
						"El título pasa. Llévate a Bahamut y elige la última carta de esta campaña."),
					say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"O título fica. A revanche é agora, na mesma mesa.",
						"The title stays. The rematch is now, at the same table.",
						"El título se queda. La revancha es ahora, en la misma mesa."),
					new Line[] {
					N("Vitória de {name}! {O campeão|A campeã} da mesa leva a carta fixa deste chefe e ainda escolhe mais uma no deck adversário.",
						"Win for {name}! The table winner takes this boss's fixed card and also picks one more from the opponent's deck.",
						"¡Victoria de {name}! {El campeón|La campeona} de la mesa se lleva la carta fija de este jefe y elige una más del mazo rival.")
					},
					new Line[] {
					N("Derrota de {name}. Reconhecimento esportivo: a mesa fica aberta. Pode tentar de novo, sem perder carta.",
						"Defeat for {name}. A sportsmanlike reading: the table stays open. You may try again, and you lose no card.",
						"Derrota de {name}. Reconocimiento deportivo: la mesa sigue abierta. Puedes intentarlo de nuevo, sin perder carta.")
					})
			});
	}

	private static Line N(String pt, String en, String es) {
		return new Line(true, StoryCampaign.NARRATOR_PORTRAIT, false, "Nilo Vann", "Nilo Vann", "Nilo Vann", pt, en, es);
	}

	private static Line Q(String pt, String en, String es) {
		return new Line(false, "squall", false, "Squall", "Squall", "Squall", pt, en, es);
	}

	private static Line Q(String pt, String en, String es, StoryRuleExample example) {
		return Q(pt, en, es).withExample(example);
	}

	private static Line say(String portrait, String npt, String nen, String nes, String pt, String en, String es) {
		return new Line(false, portrait, false, npt, nen, nes, pt, en, es);
	}

	private static Line lost(String portrait, String npt, String nen, String nes, String pt, String en, String es) {
		return new Line(false, portrait, true, npt, nen, nes, pt, en, es);
	}

	/** CC King reveal: the lines play over {@link StoryCampaign#KING_REVEAL}. */
	private static Line reveal(Line line) {
		return line.withScene(StoryCampaign.KING_REVEAL);
	}

	private static Line[] reveal(Line[] lines) {
		Line[] out = new Line[lines.length];
		for (int i = 0; i < lines.length; i++)
			out[i] = reveal(lines[i]);
		return out;
	}

	/**
	 * The opponent shows the defeat face only when the student wins:
	 * {@code win} and the opponent's own lines in {@code afterWin} use {@code _derrota};
	 * {@code lose} and {@code afterLose} keep the normal face.
	 */
	static Line beaten(Line line, String portrait, boolean lost) {
		if (line == null || line.narrator || portrait == null || !portrait.equals(line.portrait))
			return line;
		return line.withDefeat(lost);
	}

	private static Line[] beaten(Line[] lines, String portrait, boolean lost) {
		if (lines == null)
			return null;
		Line[] out = new Line[lines.length];
		for (int i = 0; i < lines.length; i++)
			out[i] = beaten(lines[i], portrait, lost);
		return out;
	}

	private static Region region(String id, String video, String background, Kind kind, int cutoff, String[][] npc,
			String npt, String nen, String nes, Line[] arrival, Duel[] duels) {
		return new Region(id, video, background, kind, cutoff, npc, npt, nen, nes, arrival, duels);
	}

	private static Duel duel(String id, String portrait, String npt, String nen, String nes,
			int min, int max, Options.AIType ai, Ruleset rules, int reward, boolean inTable,
			String rolePt, String roleEn, String roleEs, String stylePt, String styleEn, String styleEs,
			Line[] bio, Line[] open, Line before, Line win, Line lose, Line[] afterWin, Line[] afterLose) {
		return new Duel(id, portrait, npt, nen, nes, min, max, ai, rules, reward, inTable,
			rolePt, roleEn, roleEs, stylePt, styleEn, styleEs, bio, open, before,
			beaten(win, portrait, true), beaten(lose, portrait, false),
			beaten(afterWin, portrait, true), beaten(afterLose, portrait, false));
	}
}
