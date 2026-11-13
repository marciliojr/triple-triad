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

import itdelatrisu.tripletriad.Options;
import itdelatrisu.tripletriad.story.StoryCampaign.Duel;
import itdelatrisu.tripletriad.story.StoryCampaign.Kind;
import itdelatrisu.tripletriad.story.StoryCampaign.Line;
import itdelatrisu.tripletriad.story.StoryCampaign.Region;
import itdelatrisu.tripletriad.story.StoryCampaign.Ruleset;

/**
 * Fan-story script for the World Triple Triad Tournament.
 * Nilo Vann calls the matches. Squall mentors and does not play.
 */
final class StoryScript {
	private StoryScript() {}

	static Line[] starter() {
		return new Line[] {
			say("cc_spade", "CC Spade", "CC Spade", "CC Spade",
				"Album curto demais pra seletiva. Toma cinco cartas. Depois a gente joga de verdade.",
				"The album is too thin for the qualifier. Take five cards. Then we play for real.",
				"El album no da para la selectiva. Toma cinco cartas. Despues jugamos de verdad.")
		};
	}

	static Line[] ceremony() {
		return new Line[] {
			N("{name}. O titulo fica com {o aluno|a aluna} de Balamb. {Campeão|Campeã} mundial de Triple Triad.",
				"{name}. The title stays with the Balamb student. World Triple Triad champion.",
				"{name}. El titulo se queda con {el alumno|la alumna} de Balamb. {Campeón|Campeona} mundial de Triple Triad."),
			Q("Voce jogou. Eu so vi. O nome na ficha era o seu desde o primeiro dia.",
				"You played. I only watched. The name on the sheet was yours from day one.",
				"Jugaste tu. Yo solo mire. El nombre en la ficha era el tuyo desde el primer dia."),
			say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
				"A mesa reconhece {name}. Quando quiser, eu sento de novo. O titulo, hoje, e seu.",
				"The table recognizes {name}. Whenever you want, I'll sit again. The title, today, is yours.",
				"La mesa reconoce a {name}. Cuando quieras, me siento otra vez. El titulo, hoy, es tuyo."),
			say("seifer", "Seifer", "Seifer", "Seifer",
				"...Nao acostuma, {name}. Ano que vem a cadeira do meio e minha.",
				"...Don't get used to it, {name}. Next year the middle chair is mine.",
				"...No te acostumbres, {name}. El ano que viene la silla del medio es mia."),
			N("Encerro o Torneio Mundial. {name}, de Balamb Garden, e {o campeão|a campeã}.",
				"That closes the World Tournament. {name}, of Balamb Garden, is the champion.",
				"Cierro el Torneo Mundial. {name}, de Balamb Garden, es {el campeón|la campeona}.")
		};
	}

	static Region[] build() {
		return new Region[] { garden(), town(), timber(), deling(), dollet(), fh(), esthar(), fin() };
	}

	private static Region garden() {
		return region("garden", "intro_historia", "mesa_balamb_garden.png", Kind.LADDER, 0, null,
			"Garden - Seletiva", "Garden - Qualifier", "Garden - Selectiva",
			new Line[] {
				N("Aqui e Nilo Vann, locutor do Torneio Mundial de Triple Triad. A inscricao abre em Balamb Garden.",
					"This is Nilo Vann, calling the World Triple Triad Tournament. Registration opens at Balamb Garden.",
					"Aqui Nilo Vann, locutor del Torneo Mundial de Triple Triad. La inscripcion abre en Balamb Garden."),
				Q("{name}. Eu nao sento. Squall fica do lado de fora da mesa. O nome na ficha e o seu.",
					"{name}. I'm not sitting. Squall stays off the table. The name on the sheet is yours.",
					"{name}. Yo no me siento. Squall se queda fuera de la mesa. El nombre en la ficha es el tuyo."),
				N("A seletiva e uma chave continua. Vitoria enfrenta o proximo cabeca. Derrota repete a mesma chave, sem eliminar.",
					"The qualifier is a ladder. A win faces the next seed. A loss replays the same tie and does not eliminate you.",
					"La selectiva es una llave continua. La victoria enfrenta al siguiente cabeza. La derrota repite la misma llave, sin eliminar."),
				Q("O Seifer tambem se inscreveu. Ele sobe outra escada. O seu caminho passa pela Xu e termina na Quistis.",
					"Seifer signed up too. He climbs another ladder. Your path goes through Xu and ends at Quistis.",
					"Seifer tambien se inscribio. Sube otra escalera. Tu camino pasa por Xu y termina en Quistis."),
				N("Quem vence a final da seletiva entra no mundial. {name}, a primeira cadeira e a da Lina Faris.",
					"Whoever wins the qualifier final enters the world event. {name}, the first chair is Lina Faris.",
					"Quien gane la final de la selectiva entra al mundial. {name}, la primera silla es la de Lina Faris.")
			},
			new Duel[] {
				duel("cadete", "npc_cadete", "Lina Faris", "Lina Faris", "Lina Faris", 1, 2,
					Options.AIType.DEFENSIVE, Ruleset.OPEN_BASIC, 0, false,
					"Inscricao", "Entry match", "Inscripcion",
					"Mao aberta e jogo curto. Ela mostra as cartas.",
					"Open hand, short game. She shows her cards.",
					"Mano abierta y partida corta. Ella muestra las cartas.",
					new Line[] {
						N("Lina Faris, caloura de Balamb. Entrou porque a inscricao do Garden e aberta a quem estuda aqui.", "Lina Faris, a Balamb freshman. She entered because Garden registration is open to anyone who studies here.", "Lina Faris, estudiante de primer ano de Balamb. Entro porque la inscripcion del Garden esta abierta a quien estudia aqui."),
						N("Quer provar que tem cadeira no corredor, nao so na sala. Joga devagar e com a mao aberta.", "She wants to prove she has a chair in the hall, not only in class. She plays slowly, with an open hand.", "Quiere probar que tiene silla en el pasillo, no solo en el aula. Juega despacio y con la mano abierta."),
						N("E a partida que escreve o nome de {name} na ficha. Sem carta especial: so a vaga.", "This is the match that writes {name}'s name on the sheet. No special card: only the spot.", "Es la partida que escribe el nombre de {name} en la ficha. Sin carta especial: solo la plaza.")
					},
					new Line[] {
						N("Balamb Garden, partida de inscricao. Lina Faris, caloura, contra {name}.", "Balamb Garden, entry match. Lina Faris, freshman, against {name}.", "Balamb Garden, partida de inscripcion. Lina Faris, de primer ano, contra {name}."),
						N("Estilo aberto e defensivo. Nao conto a mao. A vaga no mundial comeca nesta cadeira.", "Open, defensive style. I won't read the hand. The world-tournament spot starts in this chair.", "Estilo abierto y defensivo. No cuento la mano. La plaza del mundial empieza en esta silla.")
					},
					say("npc_cadete", "Lina Faris", "Lina Faris", "Lina Faris",
						"Se eu ganhar, pelo menos meu nome fica na lista. Mao aberta, combinado?",
						"If I win, at least my name stays on the list. Open hand, all right?",
						"Si gano, al menos mi nombre queda en la lista. Mano abierta, ¿de acuerdo?"),
					say("npc_cadete", "Lina Faris", "Lina Faris", "Lina Faris",
						"Seu nome entra. Eu fico na plateia e torco sem vergonha.",
						"Your name goes in. I'll be in the stands, cheering without shame.",
						"Tu nombre entra. Yo me quedo en la grada y animo sin verguenza."),
					lost("npc_cadete", "Lina Faris", "Lina Faris", "Lina Faris",
						"Ainda da. Olha o lado que encosta antes de soltar.",
						"Still time. Look at the side that touches before you let go.",
						"Todavia da. Mira el lado que toca antes de soltar."),
					new Line[] {
						N("Inscricao aceita. {name} esta no chaveamento. A proxima cadeira e o CC Spade.", "Entry accepted. {name} is on the ladder. The next chair is CC Spade.", "Inscripcion aceptada. {name} esta en la llave. La proxima silla es CC Spade."),
						Q("Nome na ficha. Agora e mesa de verdade.", "Name on the sheet. Now it's a real table.", "Nombre en la ficha. Ahora es mesa de verdad.")
					},
					new Line[] {
						N("A inscricao nao fecha. A regra da seletiva e revanche na mesma chave. Lina continua sentada.", "Registration doesn't close. The qualifier rule is a rematch of the same tie. Lina stays seated.", "La inscripcion no se cierra. La regla de la selectiva es la revancha en la misma llave. Lina sigue sentada.")
					}),
				duel("spade", "cc_spade", "CC Spade", "CC Spade", "CC Spade", 1, 3,
					Options.AIType.DEFENSIVE, Ruleset.BASIC, 0, false,
					"1a chave", "1st tie", "1a llave",
					"Defensivo. Segura o canto e espera o erro.",
					"Defensive. He holds a corner and waits for the mistake.",
					"Defensivo. Aguanta la esquina y espera el error.",
					new Line[] {
						N("CC Spade guarda o caixa do grupo e empresta carta a quem chega sem album.", "CC Spade keeps the club's box and lends cards to anyone who arrives without an album.", "CC Spade guarda la caja del grupo y presta cartas a quien llega sin album."),
						N("Entrou na seletiva para o CC Group existir fora do corredor. Joga paciente, quase sem falar.", "He entered the qualifier so the CC Group would exist outside the hall. He plays patiently, almost without talking.", "Entro en la selectiva para que el CC Group exista fuera del pasillo. Juega paciente, casi sin hablar."),
						N("Esta e a primeira chave de verdade. Same entra. Sem carta especial.", "This is the first real tie. Same is on. No special card.", "Esta es la primera llave de verdad. Entra Same. Sin carta especial.")
					},
					new Line[] {
						N("Primeira chave da seletiva. CC Spade, do CC Group, contra {name}.", "First tie of the qualifier. CC Spade, of the CC Group, against {name}.", "Primera llave de la selectiva. CC Spade, del CC Group, contra {name}."),
						N("Estilo defensivo. Ele nao entrega o canto. Quem vence enfrenta o Joker.", "Defensive style. He doesn't give up the corner. The winner faces Joker.", "Estilo defensivo. El no entrega la esquina. Quien gana enfrenta a Joker.")
					},
					say("cc_spade", "CC Spade", "CC Spade", "CC Spade",
						"As cinco de emprestimo acabam aqui. Mostra o que fez com elas.",
						"The five I lent you end here. Show me what you did with them.",
						"Las cinco que te preste terminan aqui. Muestrame que hiciste con ellas."),
					say("cc_spade", "CC Spade", "CC Spade", "CC Spade",
						"Fica com a vaga. O Joker esta no centro de treino, e ele nao empresta piada.",
						"Keep the spot. Joker's at the training center, and he doesn't lend out jokes.",
						"Quedate el lugar. Joker esta en el centro de entrenamiento, y no presta chistes."),
					lost("cc_spade", "CC Spade", "CC Spade", "CC Spade",
						"Cedo. Volta quando Same deixar de ser so um nome.",
						"Too soon. Come back when Same is more than a word.",
						"Demasiado pronto. Vuelve cuando Same sea mas que una palabra."),
					new Line[] {
						N("Primeira chave de {name}. O Joker espera no centro de treino.", "{name}'s first tie. Joker is waiting at the training center.", "Primera llave de {name}. Joker espera en el centro de entrenamiento."),
						Q("Spade joga como quem tem tempo. O proximo, nao.", "Spade plays like someone who has time. The next one doesn't.", "Spade juega como quien tiene tiempo. El siguiente, no.")
					},
					new Line[] {
						N("Derrota nesta chave nao risca o nome. A revanche e contra o Spade, agora.", "A loss in this tie doesn't scratch the name. The rematch is against Spade, now.", "Una derrota en esta llave no tacha el nombre. La revancha es contra Spade, ahora.")
					}),
				duel("joker", "cc_joker", "CC Joker", "CC Joker", "CC Joker", 2, 4,
					Options.AIType.DEFENSIVE, Ruleset.BASIC, 0, false,
					"2a chave", "2nd tie", "2a llave",
					"Defensivo com trocas curtas. Punir pressa.",
					"Defensive, with short trades. He punishes a rush.",
					"Defensivo, con cambios cortos. Castiga la prisa.",
					new Line[] {
						N("CC Joker joga no centro de treino, longe do quadro de avisos do grupo.", "CC Joker plays at the training center, away from the club notice board.", "CC Joker juega en el centro de entrenamiento, lejos del tablon del grupo."),
						N("Entrou para mostrar que o CC Group nao e so sala de aula. O humor fica fora da carta.", "He entered to show the CC Group isn't only a classroom. The humor stays off the card.", "Entro para mostrar que el CC Group no es solo un aula. El humor se queda fuera de la carta."),
						N("Segunda chave. Ainda sem premio especial. O Club vem depois, com Plus.", "Second tie. Still no special prize. Club comes next, with Plus.", "Segunda llave. Todavia sin premio especial. Club viene despues, con Plus.")
					},
					new Line[] {
						N("Segunda chave. CC Joker contra {name}, no centro de treino do Garden.", "Second tie. CC Joker against {name}, at Garden's training center.", "Segunda llave. CC Joker contra {name}, en el centro de entrenamiento del Garden."),
						N("Ele troca pouco e espera. A vaga seguinte e o CC Club.", "He trades little and waits. The next spot is CC Club.", "Cambia poco y espera. La plaza siguiente es CC Club.")
					},
					say("cc_joker", "CC Joker", "CC Joker", "CC Joker",
						"Aqui eu troco carta, nao piada. O Spade ja fez a parte dele.",
						"Out here I trade cards, not jokes. Spade already did his part.",
						"Aqui cambio cartas, no chistes. Spade ya hizo su parte."),
					say("cc_joker", "CC Joker", "CC Joker", "CC Joker",
						"Tá valendo. O Club trata Plus como exercicio. Nao ri com ele.",
						"That counts. Club treats Plus like a drill. Don't laugh with him.",
						"Eso vale. Club trata Plus como un ejercicio. No te rias con el."),
					lost("cc_joker", "CC Joker", "CC Joker", "CC Joker",
						"Sem pressa. Eu nao saio desta cadeira.",
						"No rush. I'm not leaving this chair.",
						"Sin prisa. Yo no me levanto de esta silla."),
					new Line[] {
						N("{name} passa o Joker. Proxima chave: CC Club, e Plus entra na mesa.", "{name} gets past Joker. Next tie: CC Club, and Plus comes onto the table.", "{name} pasa a Joker. Proxima llave: CC Club, y Plus entra en la mesa."),
						Q("Ele fala pouco quando perde. Bom sinal.", "He talks little when he loses. Good sign.", "Habla poco cuando pierde. Buena senal.")
					},
					new Line[] {
						N("A segunda chave continua de pe. Revanche contra o Joker, mesma regra.", "The second tie is still standing. Rematch against Joker, same rule.", "La segunda llave sigue en pie. Revancha contra Joker, misma regla.")
					}),
				duel("club", "cc_club", "CC Club", "CC Club", "CC Club", 3, 4,
					Options.AIType.BALANCED, Ruleset.PLUS, 0, false,
					"Quartas da seletiva", "Qualifier quarterfinal", "Cuartos de la selectiva",
					"Plus e Combo. Joga como exercicio de campo.",
					"Plus and Combo. He plays it like a field drill.",
					"Plus y Combo. Lo juega como un ejercicio de campo.",
					new Line[] {
						N("CC Club e candidato a SeeD. Para ele, a seletiva e mais um treino com plateia.", "CC Club is a SeeD candidate. For him the qualifier is one more drill with an audience.", "CC Club es candidato a SeeD. Para el, la selectiva es un entrenamiento mas con publico."),
						N("Quer um registro limpo antes da prova de campo. Nao empresta carta e nao conta historia.", "He wants a clean record before the field exam. He doesn't lend cards or tell stories.", "Quiere un registro limpio antes del examen de campo. No presta cartas ni cuenta historias."),
						N("Quartas da seletiva. Plus e Combo. A Xu espera na semi.", "Qualifier quarterfinal. Plus and Combo. Xu is waiting in the semi.", "Cuartos de la selectiva. Plus y Combo. Xu espera en la semi.")
					},
					new Line[] {
						N("Quartas da seletiva do Garden. CC Club contra {name}.", "Garden qualifier quarterfinal. CC Club against {name}.", "Cuartos de la selectiva del Garden. CC Club contra {name}."),
						N("Plus esta ligado. Ele soma os lados como quem confere um mapa. A vaga e a semifinal.", "Plus is on. He adds the sides like someone checking a map. The spot is the semifinal.", "Plus esta activo. Suma los lados como quien revisa un mapa. La plaza es la semifinal.")
					},
					say("cc_club", "CC Club", "CC Club", "CC Club",
						"SeeD tambem senta. Hoje Same e Plus andam juntos. Sem conversa no meio.",
						"SeeDs sit down too. Today Same and Plus come together. No talk in the middle.",
						"Los SeeD tambien se sientan. Hoy Same y Plus van juntos. Sin charla en medio."),
					say("cc_club", "CC Club", "CC Club", "CC Club",
						"Registro aceito. A Xu nao facilita nem para colega de grupo.",
						"Record accepted. Xu doesn't make it easy, not even for a clubmate.",
						"Registro aceptado. Xu no lo pone facil, ni para un companero del grupo."),
					lost("cc_club", "CC Club", "CC Club", "CC Club",
						"Plus pune pressa. De novo, somando antes de soltar.",
						"Plus punishes a rush. Again, add before you let go.",
						"Plus castiga la prisa. Otra vez, suma antes de soltar."),
					new Line[] {
						N("Quartas superadas. {name} enfrenta a Xu na semifinal da seletiva.", "Quarterfinal cleared. {name} faces Xu in the qualifier semifinal.", "Cuartos superados. {name} enfrenta a Xu en la semifinal de la selectiva."),
						Q("Ele joga direito. A Xu joga para ganhar.", "He plays correctly. Xu plays to win.", "El juega correcto. Xu juega para ganar.")
					},
					new Line[] {
						N("Quartas sem eliminacao definitiva. A revanche e esta mesa, com Plus ligado.", "The quarterfinal has no final elimination. The rematch is this table, with Plus on.", "Los cuartos no eliminan del todo. La revancha es esta mesa, con Plus activo.")
					}),
				duel("heart", "cc_heart", "Xu", "Xu", "Xu", 3, 5,
					Options.AIType.BALANCED, Ruleset.PLUS, 0, false,
					"Semifinal da seletiva", "Qualifier semifinal", "Semifinal de la selectiva",
					"Analitica. Plus primeiro, conversa nenhuma.",
					"Analytical. Plus first, no conversation.",
					"Analitica. Plus primero, ninguna charla.",
					new Line[] {
						N("Xu, a Heart do CC Group. Ajudante de instrucao, nao e a Quistis e nao fala como ela.", "Xu, the Heart of the CC Group. An instruction aide. She is not Quistis and she doesn't talk like her.", "Xu, la Heart del CC Group. Ayudante de instruccion. No es Quistis y no habla como ella."),
						N("Entrou para ver se as notas dela sobre Plus sobrevivem a uma mesa ao vivo.", "She entered to see whether her notes on Plus survive a live table.", "Entro para ver si sus notas sobre Plus sobreviven a una mesa en vivo."),
						N("Semifinal. Quem passa senta na final contra a Quistis, e o Ifrit esta naquela mesa.", "Semifinal. Whoever passes sits in the final against Quistis, and Ifrit is on that table.", "Semifinal. Quien pasa se sienta en la final contra Quistis, e Ifrit esta en esa mesa.")
					},
					new Line[] {
						N("Semifinal da seletiva. Xu, do CC Group, contra {name}.", "Qualifier semifinal. Xu, of the CC Group, against {name}.", "Semifinal de la selectiva. Xu, del CC Group, contra {name}."),
						N("Ela joga Plus sem enfeite. A vaga e a final do Garden.", "She plays Plus with no decoration. The spot is the Garden final.", "Juega Plus sin adorno. La plaza es la final del Garden.")
					},
					say("cc_heart", "Xu", "Xu", "Xu",
						"Xu. Eu nao entrego a mesa sorrindo. A Quistis que espere o resultado.",
						"Xu. I don't hand over the table with a smile. Quistis can wait for the result.",
						"Xu. No entrego la mesa sonriendo. Quistis que espere el resultado."),
					say("cc_heart", "Xu", "Xu", "Xu",
						"Passou. Nao pede conselho. A Quistis e a ultima porta, e ela explica depois.",
						"You passed. Don't ask me for advice. Quistis is the last door, and she explains afterwards.",
						"Pasaste. No me pidas consejo. Quistis es la ultima puerta, y ella explica despues."),
					lost("cc_heart", "Xu", "Xu", "Xu",
						"Soma de novo. Eu anoto o mesmo erro uma vez so.",
						"Add again. I write the same mistake down once.",
						"Suma otra vez. Yo anoto el mismo error una sola vez."),
					new Line[] {
						N("{name} na final da seletiva. A adversaria e a Quistis. O Ifrit sai so com a vitoria.", "{name} is in the qualifier final. The opponent is Quistis. Ifrit leaves only with the win.", "{name} esta en la final de la selectiva. La adversaria es Quistis. Ifrit sale solo con la victoria."),
						Q("A Xu respeita resultado, nao discurso. Lembra disso.", "Xu respects a result, not a speech. Remember that.", "Xu respeta el resultado, no el discurso. Recuerdalo.")
					},
					new Line[] {
						N("A semi nao fecha com derrota. Revanche contra a Xu. A final continua esperando.", "The semi doesn't close on a loss. Rematch against Xu. The final keeps waiting.", "La semi no se cierra con una derrota. Revancha contra Xu. La final sigue esperando.")
					}),
				duel("king", "cc_king", "Quistis", "Quistis", "Quistis", 4, 5,
					Options.AIType.BALANCED, Ruleset.PLUS, 85, false,
					"Final da seletiva", "Qualifier final", "Final de la selectiva",
					"Experiente. Plus e Combo, leitura longa.",
					"Experienced. Plus and Combo, a long read.",
					"Experta. Plus y Combo, lectura larga.",
					new Line[] {
						N("Quistis Trepe, instrutora e a King do CC Group. Nao e a Xu: ela explica, a Xu so mede.", "Quistis Trepe, instructor and King of the CC Group. She isn't Xu: she explains, Xu only measures.", "Quistis Trepe, instructora y King del CC Group. No es Xu: ella explica, Xu solo mide."),
						N("Entrou para a ultima palavra do Garden ser de quem aguenta explicar a derrota.", "She entered so Garden's last word would belong to someone who can explain a loss.", "Entro para que la ultima palabra del Garden sea de quien aguanta explicar la derrota."),
						N("O Ifrit e o premio fixo desta final. Nao esta na mao comum. So sai na primeira vitoria.", "Ifrit is this final's fixed prize. It is not in the common hand. It leaves only on the first win.", "Ifrit es el premio fijo de esta final. No esta en la mano comun. Solo sale en la primera victoria.")
					},
					new Line[] {
						N("Final da seletiva de Balamb Garden. Quistis contra {name}.", "Balamb Garden qualifier final. Quistis against {name}.", "Final de la selectiva de Balamb Garden. Quistis contra {name}."),
						N("Plus e Combo. Ela conduz a mesa. Em jogo: a vaga no Torneio Mundial e, na primeira vitoria, o Ifrit.", "Plus and Combo. She runs the table. At stake: a spot in the World Tournament and, on the first win, Ifrit.", "Plus y Combo. Ella conduce la mesa. En juego: la plaza en el Torneo Mundial y, en la primera victoria, Ifrit.")
					},
					say("cc_king", "Quistis", "Quistis", "Quistis",
						"{name}. Se a partida for sua, o Ifrit vai junto. Eu continuo dando aula de tarde.",
						"{name}. If the match is yours, Ifrit goes with it. I still teach in the afternoon.",
						"{name}. Si la partida es tuya, Ifrit se va contigo. Yo sigo dando clase por la tarde."),
					say("cc_king", "Quistis", "Quistis", "Quistis",
						"O Ifrit e seu. No porto de Balamb comeca o grupo. Nao confunda isso com o fim.",
						"Ifrit is yours. The group starts at Balamb harbor. Don't confuse that with the end.",
						"Ifrit es tuyo. El grupo empieza en el puerto de Balamb. No confundas eso con el final."),
					lost("cc_king", "Quistis", "Quistis", "Quistis",
						"Ainda nao. Respira, soma, e senta de novo. A final nao muda de nome.",
						"Not yet. Breathe, add, and sit again. The final doesn't change its name.",
						"Todavia no. Respira, suma, y sientate otra vez. La final no cambia de nombre."),
					new Line[] {
						N("{name} vence a seletiva e entra no mundial. Proxima etapa: classificatoria no porto de Balamb.", "{name} wins the qualifier and enters the world event. Next stage: the qualifying group at Balamb harbor.", "{name} gana la selectiva y entra al mundial. Proxima etapa: clasificatoria en el puerto de Balamb."),
						Q("Garden feito. No porto a tabela passa a valer ponto. Eu vou junto.", "Garden's done. At the harbor the table starts scoring points. I'm coming along.", "Garden hecho. En el puerto la tabla empieza a valer puntos. Yo voy.")
					},
					new Line[] {
						N("A final da seletiva da revanche na hora. A vaga no mundial so passa com a sua vitoria.", "The qualifier final rematches on the spot. The world-tournament spot passes only with your win.", "La final de la selectiva da revancha al momento. La plaza del mundial solo pasa con tu victoria.")
					})
			});
	}

	private static Region town() {
		return region("town", "chegada_balamb_town", "mesa_balamb_town.png", Kind.GROUP, 2,
			new String[][] {{"queen_town", "raijin"}, {"queen_town", "fujin"}, {"raijin", "fujin"}},
			"Balamb - Classificatoria", "Balamb - Qualifying", "Balamb - Clasificatoria",
			new Line[] {
				Q("Voce ganhou a final do Garden. O Ifrit ja e seu. Isto aqui e outra fase.", "You won the Garden final. Ifrit is already yours. This is another phase.", "Ganaste la final del Garden. Ifrit ya es tuyo. Esto es otra fase."),
				N("O porto sedia o Grupo Porto porque o salao do hotel cabe quatro mesas e o cais traz a turma.", "The harbor hosts Group Port because the hotel hall fits four tables and the dock brings the field.", "El puerto sede el Grupo Puerto porque el salon del hotel cabe cuatro mesas y el muelle trae a la gente."),
				N("Quatro jogadores, todos contra todos. Vitoria vale 3. Os outros jogos ja estao na tabela. Classificam os dois primeiros.", "Four players, all against all. A win is worth 3. The other matches are already on the table. The top two qualify.", "Cuatro jugadores, todos contra todos. La victoria vale 3. Los otros partidos ya estan en la tabla. Clasifican los dos primeros."),
				Q("{name}, voce precisa fechar em primeiro ou segundo. A Rainha recusou o bye e entrou neste grupo.", "{name}, you need to finish first or second. The Queen refused the bye and entered this group.", "{name}, tienes que cerrar primero o segundo. La Reina rechazo el bye y entro en este grupo."),
				say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
					"Eu defendo o titulo em publico, nao no papel. Se eu passar em segundo, a gente se ve mais adiante.",
					"I defend the title in public, not on paper. If I go through in second, we'll meet further on.",
					"Defiendo el titulo en publico, no en el papel. Si paso segunda, nos vemos mas adelante.")
			},
			new Duel[] {
				duel("queen_town", "card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas", 4, 5,
					Options.AIType.BALANCED, Ruleset.PLUS, 84, true,
					"Classificatoria", "Qualifying match", "Clasificatoria",
					"Controle de canto e Plus. Nao abre a mao.",
					"Corner control and Plus. She doesn't open her hand.",
					"Control de esquina y Plus. No abre la mano.",
					new Line[] {
						N("A Rainha das Cartas percorre o circuito. Recusou folga de cabeca de chave para jogar o Grupo Porto.", "The Queen of Cards walks the circuit. She refused a seed bye to play Group Port.", "La Reina de las Cartas recorre el circuito. Rechazo la exencion de cabeza para jugar el Grupo Puerto."),
						N("Quer o titulo defendido na frente de quem chegou de barco. Trata {name} como estreante, ainda sem ironia.", "She wants the title defended in front of the people who came by boat. She still treats {name} as a newcomer, without irony.", "Quiere el titulo defendido delante de quien llego en barco. Trata a {name} como debutante, todavia sin ironia."),
						N("A Shiva e o premio fixo deste jogo. Nao entra na mao. Na primeira vitoria ela muda de dono, alem da carta escolhida.", "Shiva is this match's fixed prize. She is not in the hand. On the first win she changes owners, besides the chosen card.", "Shiva es el premio fijo de este partido. No entra en la mano. En la primera victoria cambia de dueno, ademas de la carta elegida.")
					},
					new Line[] {
						N("Balamb Town, Grupo Porto. A Rainha das Cartas contra {name}.", "Balamb Town, Group Port. The Queen of Cards against {name}.", "Balamb Town, Grupo Puerto. La Reina de las Cartas contra {name}."),
						N("Plus. Ela segura canto. Tres pontos neste jogo. Os dois primeiros do grupo seguem para a fase de Timber.", "Plus. She holds the corner. Three points in this match. The group's top two go on to the Timber phase.", "Plus. Ella aguanta la esquina. Tres puntos en este partido. Los dos primeros del grupo siguen a la fase de Timber.")
					},
					say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"A Shiva esta comigo faz tempo. Se a mesa for sua, ela viaja. O grupo, eu ainda pretendo liderar.",
						"Shiva's been with me a while. If the table is yours, she travels. I still plan to lead the group.",
						"Shiva lleva tiempo conmigo. Si la mesa es tuya, ella viaja. El grupo yo todavia pienso liderarlo."),
					say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"Leva a Shiva. Tres pontos seus. Nao cante titulo: ainda tem Raijin e Fujin.",
						"Take Shiva. Three points to you. Don't sing about the title: Raijin and Fujin are still here.",
						"Llevate a Shiva. Tres puntos tuyos. No cantes el titulo: aun estan Raijin y Fujin."),
					lost("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"Quase. A Shiva continua na bolsa, e o ponto nao entra.",
						"Close. Shiva stays in the bag, and the point doesn't go up.",
						"Casi. Shiva sigue en la bolsa, y el punto no sube."),
					new Line[] {
						N("Tres pontos para {name}. A Rainha segue no grupo: derrota nao elimina nesta fase. Raijin e o proximo.", "Three points for {name}. The Queen stays in the group: a loss doesn't eliminate in this phase. Raijin is next.", "Tres puntos para {name}. La Reina sigue en el grupo: la derrota no elimina en esta fase. Raijin es el siguiente."),
						Q("Ela nao saiu do torneio. So perdeu a mesa. Joga os outros dois.", "She didn't leave the tournament. She only lost the table. Play the other two.", "Ella no salio del torneo. Solo perdio la mesa. Juega los otros dos.")
					},
					new Line[] {
						N("Zero ponto, e a derrota nao fica na tabela. A revanche substitui o resultado quando voce vencer.", "Zero points, and the loss stays off the table. The rematch replaces the result when you win.", "Cero puntos, y la derrota no queda en la tabla. La revancha sustituye el resultado cuando ganes.")
					}),
				duel("raijin", "raijin", "Raijin", "Raijin", "Raijin", 4, 6,
					Options.AIType.BALANCED, Ruleset.PLUS, 79, true,
					"Classificatoria", "Qualifying match", "Clasificatoria",
					"Jogo alto e Plus. Ele empurra o centro.",
					"Loud game and Plus. He pushes the center.",
					"Juego ruidoso y Plus. Empuja el centro.",
					new Line[] {
						N("Raijin veio com a Fujin no mesmo barco. Entrou porque o salao do hotel estava barulhento e ele gosta de salao barulhento.", "Raijin came on the same boat as Fujin. He entered because the hotel hall was loud, and he likes a loud hall.", "Raijin vino en el mismo barco que Fujin. Entro porque el salon del hotel estaba ruidoso, y a el le gusta un salon ruidoso."),
						N("Quer os tres pontos e quer que a Fujin veja. Nao joga burro: joga forte e fala o tempo todo.", "He wants the three points and he wants Fujin to see it. He doesn't play dumb: he plays hard and talks the whole time.", "Quiere los tres puntos y quiere que Fujin lo vea. No juega tonto: juega fuerte y habla todo el tiempo."),
						N("O Angelo e o premio fixo. Nao esta entre as cinco da mao.", "Angelo is the fixed prize. He is not among the five in the hand.", "Angelo es el premio fijo. No esta entre las cinco de la mano.")
					},
					new Line[] {
						N("Ainda o Grupo Porto. Raijin contra {name}.", "Still Group Port. Raijin against {name}.", "Sigue el Grupo Puerto. Raijin contra {name}."),
						N("Ele joga o centro com Plus. Tres pontos. A Fujin ja perdeu para ele na outra mesa.", "He plays the center with Plus. Three points. Fujin already lost to him at the other table.", "Juega el centro con Plus. Tres puntos. Fujin ya perdio contra el en la otra mesa.")
					},
					say("raijin", "Raijin", "Raijin", "Raijin",
						"Ya, bora. O Angelo ta aqui e eu nao empresto. A Fujin disse pra eu nao entregar o meio.",
						"Yeah, let's go. Angelo's right here and I don't lend him out. Fujin told me not to give up the middle.",
						"Ya, vamos. Angelo esta aqui y no lo presto. Fujin me dijo que no entregue el medio."),
					say("raijin", "Raijin", "Raijin", "Raijin",
						"Ya... fica com o Angelo. A Fujin vai resmungar pouco. Tres pontos seus, ya.",
						"Yeah... keep Angelo. Fujin won't grumble much. Three points to you, yeah.",
						"Ya... quedate con Angelo. Fujin no va a rezongar mucho. Tres puntos tuyos, ya."),
					lost("raijin", "Raijin", "Raijin", "Raijin",
						"Ya, essa foi minha. Outra? Eu ainda to sentado.",
						"Yeah, that one was mine. Again? I'm still sitting.",
						"Ya, esta fue mia. ¿Otra? Yo sigo sentado."),
					new Line[] {
						N("{name} soma tres no Grupo Porto. Falta a Fujin para fechar a sua tabela.", "{name} adds three in Group Port. Fujin is left to close your table.", "{name} suma tres en el Grupo Puerto. Falta Fujin para cerrar tu tabla."),
						Q("Ele grita, mas a carta no centro era seria. Nao relaxa com a Fujin.", "He shouts, but the card in the center was serious. Don't relax against Fujin.", "El grita, pero la carta del centro era seria. No te relajes con Fujin.")
					},
					new Line[] {
						N("O ponto do Raijin nao entra contra voce. Revanche na mesma mesa. A tabela espera a vitoria.", "Raijin's point doesn't land against you. Rematch at the same table. The standings wait for the win.", "El punto de Raijin no entra contra ti. Revancha en la misma mesa. La tabla espera la victoria.")
					}),
				duel("fujin", "fujin", "Fujin", "Fujin", "Fujin", 5, 6,
					Options.AIType.OFFENSIVE, Ruleset.PLUS, 83, true,
					"Classificatoria", "Qualifying match", "Clasificatoria",
					"Ofensiva e curta. Plus sem aviso.",
					"Offensive and short. Plus without warning.",
					"Ofensiva y corta. Plus sin aviso.",
					new Line[] {
						N("Fujin entrou no mesmo grupo para o Raijin nao entregar a mesa, e porque quer um registro limpo so dela.", "Fujin entered the same group so Raijin wouldn't give the table away, and because she wants a clean record of her own.", "Fujin entro en el mismo grupo para que Raijin no entregara la mesa, y porque quiere un registro limpio solo suyo."),
						N("Fala pouco. Joga para a frente. Nao repete o discurso de ninguem do Garden.", "She talks little. She plays forward. She doesn't repeat anyone's speech from Garden.", "Habla poco. Juega hacia adelante. No repite el discurso de nadie del Garden."),
						N("Quezacotl e o premio fixo desta partida. Fecha o Grupo Porto se {name} vencer.", "Quezacotl is this match's fixed prize. It closes Group Port if {name} wins.", "Quezacotl es el premio fijo de esta partida. Cierra el Grupo Puerto si {name} gana.")
					},
					new Line[] {
						N("Ultimo jogo de {name} no Grupo Porto. Fujin.", "{name}'s last match in Group Port. Fujin.", "Ultimo partido de {name} en el Grupo Puerto. Fujin."),
						N("Ofensiva, Plus. Tres pontos classificam junto com o que ja esta na tabela. Os dois primeiros avancam.", "Offensive, Plus. Three points qualify together with what's already on the table. The top two advance.", "Ofensiva, Plus. Tres puntos clasifican junto con lo que ya esta en la tabla. Los dos primeros avanzan.")
					},
					say("fujin", "Fujin", "Fujin", "Fujin",
						"Rival. Quezacotl na mesa. Raijin ja falou demais.",
						"Rival. Quezacotl is on the table. Raijin already talked too much.",
						"Rival. Quezacotl en la mesa. Raijin ya hablo de mas."),
					say("fujin", "Fujin", "Fujin", "Fujin",
						"Forte. Pode levar. Nao conta vantagem no cais.",
						"Strong. Take it. Don't boast on the dock.",
						"Fuerte. Puedes llevartela. No alardes en el muelle."),
					lost("fujin", "Fujin", "Fujin", "Fujin",
						"Fraco. De novo.",
						"Weak. Again.",
						"Debil. Otra vez."),
					new Line[] {
						N("Grupo Porto encerrado para {name}. Confira a tabela: o corte e o segundo lugar. A Rainha segue no torneio pelo outro resultado.", "Group Port is closed for {name}. Check the table: the cutoff is second place. The Queen stays in the tournament on the other result.", "Grupo Puerto cerrado para {name}. Revisa la tabla: el corte es el segundo lugar. La Reina sigue en el torneo por el otro resultado."),
						Q("Timber e a rodada de grupos. Outro sorteio. A Rainha nao vem na sua chave de la.", "Timber is the group round. Another draw. The Queen is not in your bracket there.", "Timber es la ronda de grupos. Otro sorteo. La Reina no viene en tu llave de alla.")
					},
					new Line[] {
						N("Sem os tres pontos da Fujin a conta muda. Revanche agora. So a vitoria entra na tabela.", "Without Fujin's three points the math changes. Rematch now. Only the win goes on the table.", "Sin los tres puntos de Fujin la cuenta cambia. Revancha ahora. Solo la victoria entra en la tabla.")
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
				Q("No porto voce passou o corte. A tabela nao mente. Aqui o sorteio e outro.", "At the harbor you made the cutoff. The table doesn't lie. The draw here is different.", "En el puerto pasaste el corte. La tabla no miente. Aqui el sorteo es otro."),
				N("Timber sedia os grupos porque a estacao junta as linhas. O salao fica em cima dos trilhos, nao num campo de guerra.", "Timber hosts the groups because the station joins the lines. The hall sits above the tracks, not on a battlefield.", "Timber sede los grupos porque la estacion junta las lineas. El salon queda encima de las vias, no en un campo de guerra."),
				N("Grupo Trilho, cinco jogadores. Vitória vale 3. Classificam os tres primeiros para a fase principal.", "Rail Group, five players. A win is worth 3. The top three go to the main phase.", "Grupo Raíl, cinco jugadores. La victoria vale 3. Clasifican los tres primeros para la fase principal."),
				Q("{name}, a Rainha caiu em outro grupo. O Seifer tambem. O seu lado tem Zone, Jack, o Holt e a Rinoa.", "{name}, the Queen landed in another group. So did Seifer. Your side has Zone, Jack, Holt and Rinoa.", "{name}, la Reina cayo en otro grupo. Seifer tambien. Tu lado tiene a Zone, Jack, Holt y Rinoa."),
				say("rinoa", "Rinoa", "Rinoa", "Rinoa",
					"Os Owls daqui sao o circulo de cartas da estacao. Se vier jogar limpo, a gente se respeita.",
					"The Owls here are the station's card circle. Play it straight and we'll respect each other.",
					"Los Owls de aqui son el circulo de cartas de la estacion. Juega limpio y nos respetamos.")
			},
			new Duel[] {
				duel("zone", "zone_watts", "Zone e Watts", "Zone and Watts", "Zone y Watts", 4, 6,
					Options.AIType.BALANCED, Ruleset.PLUS, 82, true,
					"Grupo Trilho", "Rail Group", "Grupo Raíl",
					"Plus espalhafatoso. Zone joga, Watts filma.",
					"Flashy Plus. Zone plays, Watts films.",
					"Plus ruidoso. Zone juega, Watts filma.",
					new Line[] {
						N("Zone e Watts filmam o torneio. Entraram no Grupo Trilho com um convite local da estacao.", "Zone and Watts are filming the tournament. They entered the Rail Group on a local invite from the station.", "Zone y Watts filman el torneo. Entraron en el Grupo Raíl con una invitación local de la estación."),
						N("Querem o documentario e, ja que sentaram, querem os tres pontos. Zone fala. Watts nao solta a camera.", "They want the documentary and, since they sat down, they want the three points. Zone talks. Watts doesn't let go of the camera.", "Quieren el documental y, ya que se sentaron, quieren los tres puntos. Zone habla. Watts no suelta la cámara."),
						N("O Chicobo e o premio fixo. Nao esta na mao que eles jogam.", "Chicobo is the fixed prize. It isn't in the hand they play.", "Chicobo es el premio fijo. No está en la mano que juegan.")
					},
					new Line[] {
						N("Timber, Grupo Trilho. Zone e Watts contra {name}. Uma ficha so.", "Timber, Rail Group. Zone and Watts against {name}. One entry.", "Timber, Grupo Raíl. Zone y Watts contra {name}. Una sola ficha."),
						N("Plus, jogo de cena. Tres pontos. Os outros resultados do grupo ja estao no quadro.", "Plus, a scene-heavy game. Three points. The group's other results are already on the board.", "Plus, juego de escena. Tres puntos. Los otros resultados del grupo ya están en el cuadro.")
					},
					say("zone_watts", "Zone", "Zone", "Zone",
						"Watts, nao corta. {name}, se a cena prestar, o Chicobo entra no credito final.",
						"Watts, don't cut. {name}, if the scene works, Chicobo rolls in the credits.",
						"Watts, no cortes. {name}, si la escena funciona, Chicobo entra en los creditos."),
					say("zone_watts", "Zone", "Zone", "Zone",
						"Corta! Ficou otimo. O Chicobo e seu. A gente continua filmando quem passar.",
						"Cut! That was great. Chicobo's yours. We'll keep filming whoever goes through.",
						"¡Corten! Quedo genial. Chicobo es tuyo. Seguimos filmando a quien pase."),
					lost("zone_watts", "Zone", "Zone", "Zone",
						"De novo, do topo. Dessa vez sem atropelar o Plus. Watts, continua gravando.",
						"From the top. This time don't rush the Plus. Watts, keep rolling.",
						"Desde el principio. Esta vez sin atropellar el Plus. Watts, sigue grabando."),
					new Line[] {
						N("Tres pontos no Grupo Trilho. O proximo e o CC Jack, que veio de trem ler elemento.", "Three points in the Rail Group. Next is CC Jack, who came by train to read element.", "Tres puntos en el Grupo Raíl. El siguiente es CC Jack, que vino en tren a leer elemento."),
						Q("Nao joga para a camera. O Jack joga para o chao do tabuleiro.", "Don't play for the camera. Jack plays for the floor of the board.", "No juegues para la cámara. Jack juega para el suelo del tablero.")
					},
					new Line[] {
						N("A cena nao vale ponto. Revanche contra Zone. A tabela so muda com a vitoria.", "The scene isn't worth a point. Rematch against Zone. The table only changes with the win.", "La escena no vale punto. Revancha contra Zone. La tabla solo cambia con la victoria.")
					}),
				duel("jack", "cc_jack", "CC Jack", "CC Jack", "CC Jack", 5, 6,
					Options.AIType.BALANCED, Ruleset.ELEMENT, 80, true,
					"Grupo Trilho", "Rail Group", "Grupo Raíl",
					"Le o elemento do tabuleiro antes da carta.",
					"He reads the board's element before the card.",
					"Lee el elemento del tablero antes de la carta.",
					new Line[] {
						N("CC Jack e o leitor de elemento do CC Group. Veio de trem, nao com o Spade nem com a Xu.", "CC Jack is the CC Group's element reader. He came by train, not with Spade or Xu.", "CC Jack es el lector de elemento del CC Group. Vino en tren, no con Spade ni con Xu."),
						N("Quer provar que elemento cabe numa fase de grupos, nao so numa sala do Garden.", "He wants to prove element belongs in a group phase, not only in a Garden room.", "Quiere probar que el elemento cabe en una fase de grupos, no solo en una sala del Garden."),
						N("O Gilgamesh e o premio fixo. A regra Elemental esta ligada. Eu nao revelo a mao.", "Gilgamesh is the fixed prize. Elemental is on. I won't reveal the hand.", "Gilgamesh es el premio fijo. La regla Elemental está activa. No revelo la mano.")
					},
					new Line[] {
						N("Grupo Trilho, segunda mesa de {name}. CC Jack.", "Rail Group, {name}'s second table. CC Jack.", "Grupo Raíl, segunda mesa de {name}. CC Jack."),
						N("Elemental, Plus e Combo. Tres pontos. O Gilgamesh so sai na primeira vitoria, fora da mao.", "Elemental, Plus and Combo. Three points. Gilgamesh leaves only on the first win, outside the hand.", "Elemental, Plus y Combo. Tres puntos. Gilgamesh solo sale en la primera victoria, fuera de la mano.")
					},
					say("cc_jack", "CC Jack", "CC Jack", "CC Jack",
						"Em Timber o tabuleiro respira. O Gilgamesh olha. Eu tambem.",
						"In Timber the board breathes. Gilgamesh is watching. So am I.",
						"En Timber el tablero respira. Gilgamesh mira. Yo tambien."),
					say("cc_jack", "CC Jack", "CC Jack", "CC Jack",
						"Voce leu o chao. O Gilgamesh troca de mao. Nao conta isso como sorte.",
						"You read the ground. Gilgamesh changes hands. Don't call that luck.",
						"Leiste el suelo. Gilgamesh cambia de mano. No lo llames suerte."),
					lost("cc_jack", "CC Jack", "CC Jack", "CC Jack",
						"O elemento virou. Senta de novo e olha a casa antes da carta.",
						"The element turned. Sit again and look at the space before the card.",
						"El elemento se volvio. Sientate otra vez y mira la casilla antes de la carta."),
					new Line[] {
						N("{name} deixa o Jack para tras na tabela. Falta o sargento Holt e a Rinoa.", "{name} leaves Jack behind on the table. Sergeant Holt and Rinoa are left.", "{name} deja a Jack atrás en la tabla. Faltan el sargento Holt y Rinoa."),
						Q("Elemento nao e truque. Na proxima mesa o Holt joga reto.", "Element isn't a trick. At the next table Holt plays straight.", "El elemento no es un truco. En la próxima mesa Holt juega recto.")
					},
					new Line[] {
						N("Sem ponto contra o Jack. A revanche e nesta mesa, com Elemental. A vaga no tres primeiros ainda depende disso.", "No point against Jack. The rematch is this table, with Elemental. A top-three spot still depends on it.", "Sin punto contra Jack. La revancha es en esta mesa, con Elemental. La plaza entre los tres primeros todavía depende de esto.")
					}),
				duel("soldier_timber", "npc_soldado_galbadia", "Bram Holt", "Bram Holt", "Bram Holt", 5, 7,
					Options.AIType.OFFENSIVE, Ruleset.PLUS, 0, true,
					"Grupo Trilho", "Rail Group", "Grupo Raíl",
					"Ofensivo e direto. Sem carta premiada.",
					"Offensive and straight. No prize card.",
					"Ofensivo y directo. Sin carta de premio.",
					new Line[] {
						N("Bram Holt, sargento de folga. O trem parou, o grupo precisava de um nome local, e ele se inscreveu.", "Bram Holt, a sergeant on leave. The train stopped, the group needed a local name, and he signed up.", "Bram Holt, sargento de franco. El tren paró, el grupo necesitaba un nombre local, y él se inscribió."),
						N("Nao esta aqui a servico de ninguem. Quer jogar rapido e voltar para o turno. Nao tem carta especial.", "He isn't here in anyone's service. He wants to play fast and get back to his shift. He has no special card.", "No está aquí al servicio de nadie. Quiere jugar rápido y volver al turno. No tiene carta especial."),
						N("A mao dele e comum. {name} escolhe uma se vencer. So isso, e tres pontos.", "His hand is ordinary. {name} chooses one card on a win. That's all, plus three points.", "Su mano es común. {name} elige una si gana. Solo eso, y tres puntos.")
					},
					new Line[] {
						N("Grupo Trilho. Bram Holt contra {name}.", "Rail Group. Bram Holt against {name}.", "Grupo Raíl. Bram Holt contra {name}."),
						N("Ofensivo, Plus. Nao ha premio fixo. Ha tres pontos e uma carta da mao, se a mesa for sua.", "Offensive, Plus. There is no fixed prize. There are three points and one card from the hand, if the table is yours.", "Ofensivo, Plus. No hay premio fijo. Hay tres puntos y una carta de la mano, si la mesa es tuya.")
					},
					say("npc_soldado_galbadia", "Bram Holt", "Bram Holt", "Bram Holt",
						"Folga curta. Eu jogo reto. Se perder, o turno me espera do mesmo jeito.",
						"Short leave. I play straight. If I lose, the shift is waiting either way.",
						"Franco corto. Juego recto. Si pierdo, el turno me espera igual."),
					say("npc_soldado_galbadia", "Bram Holt", "Bram Holt", "Bram Holt",
						"Pode seguir. Ninguem me mandou segurar carta. Boa escolha na mao.",
						"Move on. Nobody told me to hold a card. Pick well from the hand.",
						"Sigue. Nadie me mando retener una carta. Elige bien de la mano."),
					lost("npc_soldado_galbadia", "Bram Holt", "Bram Holt", "Bram Holt",
						"Junta isso e tenta outra vez. Eu ainda tenho folga.",
						"Pick that up and try again. I still have leave left.",
						"Junta eso e intentalo otra vez. Aun me queda franco."),
					new Line[] {
						N("Holt soma zero contra {name}. A ultima do grupo e a Rinoa, do circulo da estacao.", "Holt scores zero against {name}. The last of the group is Rinoa, from the station circle.", "Holt suma cero contra {name}. La última del grupo es Rinoa, del círculo de la estación."),
						Q("Ela joga com gente, nao contra plateia. Trata o nome dela direito.", "She plays with people, not against an audience. Get her name right.", "Ella juega con gente, no contra el público. Trata bien su nombre.")
					},
					new Line[] {
						N("Holt nao marcou contra voce, e voce nao marcou contra ele. Revanche. O tres primeiros ainda nao e seu.", "Holt didn't score against you, and you didn't score against him. Rematch. The top three isn't yours yet.", "Holt no marcó contra ti, y tú no marcaste contra él. Revancha. Los tres primeros todavía no son tuyos.")
					}),
				duel("rinoa", "rinoa", "Rinoa", "Rinoa", "Rinoa", 5, 7,
					Options.AIType.BALANCED, Ruleset.ELEMENT, 86, true,
					"Grupo Trilho", "Rail Group", "Grupo Raíl",
					"Elemental equilibrado. Pressiona sem gritar.",
					"Balanced Elemental. She presses without shouting.",
					"Elemental equilibrado. Presiona sin gritar.",
					new Line[] {
						N("Rinoa Heartilly conduz os Owls, o circulo de cartas da estacao de Timber. Nao e um destacamento.", "Rinoa Heartilly runs the Owls, the card circle at Timber station. It isn't a squad.", "Rinoa Heartilly conduce a los Owls, el círculo de cartas de la estación de Timber. No es un destacamento."),
						N("Quer o nome de Timber no corte dos tres. Com {name}, o tom vira respeito se a mesa for limpa.", "She wants Timber's name in the top three. With {name}, the tone becomes respect if the table stays clean.", "Quiere el nombre de Timber en el corte de los tres. Con {name}, el tono se vuelve respeto si la mesa es limpia."),
						N("A Siren e o premio fixo. Fecha a sua conta no Grupo Trilho.", "Siren is the fixed prize. It closes your Rail Group account.", "Siren es el premio fijo. Cierra tu cuenta en el Grupo Raíl.")
					},
					new Line[] {
						N("Ultima mesa de {name} no Grupo Trilho. Rinoa, dos Owls da estacao.", "{name}'s last table in the Rail Group. Rinoa, of the station Owls.", "Última mesa de {name} en el Grupo Raíl. Rinoa, de los Owls de la estación."),
						N("Elemental. Tres pontos. O corte desta fase e o terceiro lugar. A Siren sai na primeira vitoria.", "Elemental. Three points. This phase's cutoff is third place. Siren leaves on the first win.", "Elemental. Tres puntos. El corte de esta fase es el tercer lugar. Siren sale en la primera victoria.")
					},
					say("rinoa", "Rinoa", "Rinoa", "Rinoa",
						"Os Owls querem uma carta que imponha. A Siren pode ser essa. E eu quero os tres pontos, {name}.",
						"The Owls want a card with weight. Siren might be the one. And I want the three points, {name}.",
						"Los Owls quieren una carta con peso. Siren puede ser esa. Y yo quiero los tres puntos, {name}."),
					say("rinoa", "Rinoa", "Rinoa", "Rinoa",
						"A Siren fica com voce. Nao precisa contar ao Seifer. Eu mesma nao conto.",
						"Siren's yours. You don't have to tell Seifer. I won't either.",
						"Siren se queda contigo. No hace falta contarle a Seifer. Yo tampoco lo cuento."),
					lost("rinoa", "Rinoa", "Rinoa", "Rinoa",
						"Quase. A gente tambem treina em cima do trilho. Senta de novo.",
						"Close. We practice above the tracks too. Sit down again.",
						"Casi. Nosotros tambien practicamos sobre la via. Sientate otra vez."),
					new Line[] {
						N("Grupo Trilho fechado para {name}. Olhe o quadro: passam tres. A fase principal e em Deling, em outro sorteio.", "Rail Group closed for {name}. Look at the board: three go through. The main phase is in Deling, in another draw.", "Grupo Raíl cerrado para {name}. Mira el cuadro: pasan tres. La fase principal es en Deling, en otro sorteo."),
						Q("A Rinoa segue se o lugar dela alcançar. A sua mesa em Deling nao e esta.", "Rinoa goes on if her place reaches. Your table in Deling isn't this one.", "Rinoa sigue si su lugar alcanza. Tu mesa en Deling no es esta.")
					},
					new Line[] {
						N("Sem este ponto a conta do grupo muda. Revanche contra a Rinoa. A derrota nao elimina, e tambem nao classifica.", "Without this point the group's math changes. Rematch against Rinoa. A loss doesn't eliminate, and it doesn't qualify either.", "Sin este punto la cuenta del grupo cambia. Revancha contra Rinoa. La derrota no elimina, y tampoco clasifica.")
					})
			});
	}

	private static Region deling() {
		return region("deling", "chegada_deling", "mesa_deling.png", Kind.GROUP, 2,
			new String[][] {{"queen_deling", "diamond"}, {"queen_deling", "soldier_deling"}, {"diamond", "soldier_deling"}},
			"Deling - Fase principal", "Deling - Main phase", "Deling - Fase principal",
			new Line[] {
				N("Timber mandou os tres primeiros de cada grupo para a fase principal. {name} passou. Eu conferi o quadro, nao um rumor.", "Timber sent each group's top three to the main phase. {name} went through. I checked the board, not a rumor.", "Timber mandó a los tres primeros de cada grupo a la fase principal. {name} pasó. Yo revisé el cuadro, no un rumor."),
				N("Deling sedia porque o salao antigo cabe o publico. E prestigio de cidade, nao desfile.", "Deling hosts it because the old hall fits the crowd. It's the city's prestige, not a parade.", "Deling la sede porque el salón antiguo cabe al público. Es prestigio de ciudad, no un desfile."),
				Q("O sorteio novo te colocou com a Diamond, a Mara Quill e a Rainha. Rinoa e Jack foram para outras chaves.", "The new draw put you with Diamond, Mara Quill and the Queen. Rinoa and Jack went to other brackets.", "El sorteo nuevo te puso con Diamond, Mara Quill y la Reina. Rinoa y Jack fueron a otras llaves."),
				N("Disputa de posicao. Tres pontos por vitoria. Os dois primeiros deste grupo levam a melhor entrada para Dollet.", "Placement. Three points per win. This group's top two take the better entry into Dollet.", "Disputa de puesto. Tres puntos por victoria. Los dos primeros de este grupo llevan la mejor entrada a Dollet."),
				say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
					"Meu grupo em Timber tambem classificou. {name}, aqui a gente briga pela cabeca, nao pela sobrevivencia.",
					"My Timber group qualified too. {name}, here we fight for the seed, not for survival.",
					"Mi grupo de Timber tambien clasifico. {name}, aqui peleamos por la cabeza, no por sobrevivir.")
			},
			new Duel[] {
				duel("diamond", "cc_diamond", "CC Diamond", "CC Diamond", "CC Diamond", 5, 7,
					Options.AIType.BALANCED, Ruleset.ELEMENT, 87, true,
					"Disputa de posicao", "Placement match", "Disputa de puesto",
					"Etiqueta e Elemental. Nao empurra a primeira carta.",
					"Manners and Elemental. He doesn't shove the first card.",
					"Etiqueta y Elemental. No empuja la primera carta.",
					new Line[] {
						N("CC Diamond joga no salao de Deling. Nao e o Jack: o Jack le o chao, a Diamond le o ambiente.", "CC Diamond plays in the Deling salon. He isn't Jack: Jack reads the floor, Diamond reads the room.", "CC Diamond juega en el salón de Deling. No es Jack: Jack lee el suelo, Diamond lee el ambiente."),
						N("Entrou para a cidade ter um nome que sabe sentar sem fazer cena. Quer a melhor chave, nao so passar.", "He entered so the city would have a name that knows how to sit without making a scene. He wants the better bracket, not just to pass.", "Entró para que la ciudad tenga un nombre que sabe sentarse sin armar escena. Quiere la mejor llave, no solo pasar."),
						N("Sacred e o premio fixo. Tres pontos na disputa de posicao.", "Sacred is the fixed prize. Three points in the placement round.", "Sacred es el premio fijo. Tres puntos en la disputa de puesto.")
					},
					new Line[] {
						N("Deling, fase principal. CC Diamond contra {name}.", "Deling, main phase. CC Diamond against {name}.", "Deling, fase principal. CC Diamond contra {name}."),
						N("Elemental, jogo contido. A vaga em jogo e posicao: os dois primeiros vao a Dollet com a melhor entrada.", "Elemental, a contained game. The spot at stake is placement: the top two go to Dollet with the better entry.", "Elemental, juego contenido. La plaza en juego es el puesto: los dos primeros van a Dollet con la mejor entrada.")
					},
					say("cc_diamond", "CC Diamond", "CC Diamond", "CC Diamond",
						"Em Deling ninguem senta sem convite. O seu chegou. Sacred continua comigo ate a mesa dizer o contrario.",
						"In Deling nobody sits without an invitation. Yours arrived. Sacred stays with me until the table says otherwise.",
						"En Deling nadie se sienta sin invitacion. La tuya llego. Sacred sigue conmigo hasta que la mesa diga lo contrario."),
					say("cc_diamond", "CC Diamond", "CC Diamond", "CC Diamond",
						"Bonito. Sacred muda de mao sem escandalo. A posicao, essa, eu vou ter de recomecar.",
						"Lovely. Sacred changes hands without a fuss. The placing, I'll have to start over.",
						"Bonito. Sacred cambia de mano sin escandalo. El puesto, ese, voy a tener que empezarlo de nuevo."),
					lost("cc_diamond", "CC Diamond", "CC Diamond", "CC Diamond",
						"A ultima carta faltou classe. Outra vez, com a mesma etiqueta.",
						"The last card lacked class. Once more, with the same manners.",
						"A la ultima carta le falto clase. Otra vez, con la misma etiqueta."),
					new Line[] {
						N("Tres pontos em Deling. A Diamond continua no grupo ate o corte. A proxima e Mara Quill.", "Three points in Deling. Diamond stays in the group until the cutoff. Next is Mara Quill.", "Tres puntos en Deling. Diamond sigue en el grupo hasta el corte. La siguiente es Mara Quill."),
						Q("Salao nao ganha partida. A carta ganha. Segue.", "A salon doesn't win a match. The card does. Keep going.", "El salón no gana la partida. La carta gana. Sigue.")
					},
					new Line[] {
						N("Posicao nao muda com derrota. Revanche contra a Diamond. O corte dos dois primeiros espera numero de verdade.", "Placement doesn't change on a loss. Rematch against Diamond. The top-two cutoff wants a real number.", "El puesto no cambia con una derrota. Revancha contra Diamond. El corte de los dos primeros espera un número de verdad.")
					}),
				duel("soldier_deling", "npc_soldado_galbadia", "Mara Quill", "Mara Quill", "Mara Quill", 6, 7,
					Options.AIType.OFFENSIVE, Ruleset.ELEMENT, 88, true,
					"Disputa de posicao", "Placement match", "Disputa de puesto",
					"Ofensiva formal. Elemental no bolso de quem organiza a noite.",
					"Formal and offensive. Elemental, from the woman who runs the salon night.",
					"Ofensiva y formal. Elemental, de quien organiza la noche del salon.",
					new Line[] {
						N("Mara Quill, capita, organiza a noite de cartas do salao. Nao e o Bram Holt: outro nome, outra cidade, o mesmo tipo de uniforme no retrato.", "Mara Quill, a captain, runs the salon's card night. She isn't Bram Holt: another name, another city, the same kind of uniform in the portrait.", "Mara Quill, capitana, organiza la noche de cartas del salón. No es Bram Holt: otro nombre, otra ciudad, el mismo tipo de uniforme en el retrato."),
						N("Joga pela equipe da casa. Quer que Deling apareca no corte, nao numa historia de quartel.", "She plays for the house staff. She wants Deling in the cutoff, not in a barracks story.", "Juega por el equipo de la casa. Quiere que Deling aparezca en el corte, no en una historia de cuartel."),
						N("O Minotauro e o premio fixo desta mesa.", "Minotaur is this table's fixed prize.", "Minotauro es el premio fijo de esta mesa.")
					},
					new Line[] {
						N("Fase principal, Deling. Mara Quill contra {name}.", "Main phase, Deling. Mara Quill against {name}.", "Fase principal, Deling. Mara Quill contra {name}."),
						N("Elemental ofensivo. Tres pontos de posicao. O Minotauro nao esta na mao comum.", "Offensive Elemental. Three placement points. Minotaur is not in the common hand.", "Elemental ofensivo. Tres puntos de puesto. Minotauro no está en la mano común.")
					},
					say("npc_soldado_galbadia", "Mara Quill", "Mara Quill", "Mara Quill",
						"Eu monto a noite e tambem sento. O Minotauro fica no bolso ate alguem merecer.",
						"I set the night and I also sit. Minotaur stays in the pocket until someone earns it.",
						"Yo armo la noche y tambien me siento. Minotauro se queda en el bolsillo hasta que alguien se lo gane."),
					say("npc_soldado_galbadia", "Mara Quill", "Mara Quill", "Mara Quill",
						"Toma. Nao abre isso no corredor so para aparecer. A tabela e que importa.",
						"Take it. Don't flash it in the hall just to be seen. The table is what matters.",
						"Toma. No lo ensenes en el pasillo solo para que te vean. La tabla es lo que importa."),
					lost("npc_soldado_galbadia", "Mara Quill", "Mara Quill", "Mara Quill",
						"Uma carta nao muda o salao. De novo, com a mesma regra.",
						"One card doesn't change the salon. Again, same rule.",
						"Una carta no cambia el salon. Otra vez, con la misma regla."),
					new Line[] {
						N("{name} soma no grupo de Deling. Fecha com a Rainha: e a briga direta pela cabeca da chave.", "{name} adds to the Deling group. Close it with the Queen: it's the direct fight for the seed.", "{name} suma en el grupo de Deling. Ciérralo con la Reina: es la pelea directa por la cabeza de llave."),
						Q("A Quill joga serio. A Rainha joga como quem ja ganhou isto uma vez. Nao e a mesma coisa.", "Quill plays serious. The Queen plays like someone who has already won this once. It isn't the same thing.", "Quill juega en serio. La Reina juega como quien ya ganó esto una vez. No es lo mismo.")
					},
					new Line[] {
						N("Sem ponto contra a Quill. Revanche. Disputa de posicao nao perdoa conta pela metade.", "No point against Quill. Rematch. Placement doesn't forgive a half count.", "Sin punto contra Quill. Revancha. La disputa de puesto no perdona una cuenta a medias.")
					}),
				duel("queen_deling", "card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas", 6, 7,
					Options.AIType.OFFENSIVE, Ruleset.ELEMENT, 95, true,
					"Disputa de posicao", "Placement match", "Disputa de puesto",
					"Ofensiva elegante. Elemental a favor do canto dela.",
					"Elegant and offensive. Elemental on her corner.",
					"Ofensiva y elegante. Elemental a favor de su esquina.",
					new Line[] {
						N("A Rainha voltou ao mesmo grupo de {name} porque os dois primeiros de Timber foram reembaralhados.", "The Queen is back in {name}'s group because Timber's top two were redrawn.", "La Reina volvió al mismo grupo de {name} porque los dos primeros de Timber fueron remezclados."),
						N("Ela ja nao trata {name} como estreante. Quer a cabeca da chave de Dollet.", "She no longer treats {name} as a newcomer. She wants the Dollet seed.", "Ya no trata a {name} como debutante. Quiere la cabeza de llave de Dollet."),
						N("Alexander e o premio fixo. Nao da para escolhe-lo na mao. Primeira vitoria somente.", "Alexander is the fixed prize. You can't choose him from the hand. First win only.", "Alexander es el premio fijo. No se puede elegirlo de la mano. Solo en la primera victoria.")
					},
					new Line[] {
						N("Deling, jogo da posicao. A Rainha das Cartas contra {name}.", "Deling, the placement match. The Queen of Cards against {name}.", "Deling, partido de puesto. La Reina de las Cartas contra {name}."),
						N("Elemental. Tres pontos que podem definir o primeiro lugar. Os dois primeiros seguem para a rodada decisiva, em Dollet.", "Elemental. Three points that can decide first place. The top two go to the decisive round, in Dollet.", "Elemental. Tres puntos que pueden definir el primer lugar. Los dos primeros siguen a la ronda decisiva, en Dollet.")
					},
					say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"{name}. O Alexander passou por este salao. Se a mesa for sua, ele vai na bagagem. A cabeca, eu ainda quero.",
						"{name}. Alexander came through this salon. If the table is yours, he goes in the luggage. I still want the seed.",
						"{name}. Alexander paso por este salon. Si la mesa es tuya, se va en el equipaje. La cabeza yo todavia la quiero."),
					say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"Leva o Alexander. Voce fica na frente neste grupo. Em Dollet a conta comeca de novo.",
						"Take Alexander. You're ahead in this group. In Dollet the count starts over.",
						"Llevate a Alexander. Quedas delante en este grupo. En Dollet la cuenta empieza de nuevo."),
					lost("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"O Alexander fica. Posicao nao se ganha quase.",
						"Alexander stays. Placement isn't won by almost.",
						"Alexander se queda. El puesto no se gana con un casi."),
					new Line[] {
						N("Confira a tabela de Deling. O corte e o segundo. Quem passa vai a Dollet confirmar a vaga no pub.", "Check the Deling table. The cutoff is second. Whoever passes goes to Dollet to confirm the spot at the pub.", "Revisa la tabla de Deling. El corte es el segundo. Quien pasa va a Dollet a confirmar la plaza en el pub."),
						Q("Dollet decide o mata-mata. Nao e passeio. A tradicao do pub e so o endereco.", "Dollet decides the knockout. It isn't a stroll. The pub's tradition is only the address.", "Dollet decide el mata-mata. No es un paseo. La tradición del pub es solo la dirección.")
					},
					new Line[] {
						N("A Rainha segurou o ponto. Revanche aqui. Sem a vitoria, a posicao que voce quer nao existe.", "The Queen held the point. Rematch here. Without the win, the place you want doesn't exist.", "La Reina retuvo el punto. Revancha aquí. Sin la victoria, el puesto que quieres no existe.")
					})
			});
	}

	private static Region dollet() {
		return region("dollet", "chegada_dollet", "mesa_dollet.png", Kind.GROUP, 2,
			new String[][] {{"queen_dollet", "pub"}, {"queen_dollet", "soldier_dollet"}, {"pub", "soldier_dollet"}},
			"Dollet - Rodada decisiva", "Dollet - Decisive round", "Dollet - Ronda decisiva",
			new Line[] {
				N("Deling mandou os dois primeiros. {name} esta entre eles. A outra vaga do seu grupo tambem.", "Deling sent the top two. {name} is among them. So is the other spot from your group.", "Deling mandó a los dos primeros. {name} está entre ellos. La otra plaza de tu grupo también."),
				N("Dollet sedia a rodada decisiva porque o segundo andar do pub recebe carta ha anos. A comissao alugou a sala, nao inventou uma guerra.", "Dollet hosts the decisive round because the pub's second floor has taken cards for years. The committee rented the room. It didn't invent a war.", "Dollet sede la ronda decisiva porque el segundo piso del pub recibe cartas desde hace años. La comisión alquiló la sala. No inventó una guerra."),
				Q("Tomas Reed e a dona da casa. Enne Doss joga na praca quando sai do turno. A Rainha veio confirmar a cabeca.", "Tomas Reed is the house. Enne Doss plays in the square when the shift ends. The Queen came to confirm the seed.", "Tomas Reed es el de la casa. Enne Doss juega en la plaza cuando sale del turno. La Reina vino a confirmar la cabeza."),
				N("Todos contra todos. Tres pontos. Os dois primeiros entram no mata-mata em lados opostos: o primeiro vai a FH, o segundo ao outro quarto.", "All against all. Three points. The top two enter the knockout on opposite sides: first goes to FH, second to the other quarter.", "Todos contra todos. Tres puntos. Los dos primeros entran al mata-mata en lados opuestos: el primero va a FH, el segundo al otro cuarto."),
				say("npc_pub_dollet", "Tomas Reed", "Tomas Reed", "Tomas Reed",
					"Senta. O Chubby Chocobo e a aposta da casa, nao o lanche. E eu quero os tres pontos tanto quanto voces.",
					"Sit down. Chubby Chocobo is the house bet, not the snack. And I want the three points as much as you do.",
					"Sientate. Chubby Chocobo es la apuesta de la casa, no el bocadillo. Y yo quiero los tres puntos tanto como ustedes.")
			},
			new Duel[] {
				duel("pub", "npc_pub_dollet", "Tomas Reed", "Tomas Reed", "Tomas Reed", 4, 6,
					Options.AIType.DEFENSIVE, Ruleset.PLUS, 78, true,
					"Rodada decisiva", "Decisive round", "Ronda decisiva",
					"Defensivo, de quem conhece a propria mesa.",
					"Defensive, from the man who knows his own table.",
					"Defensivo, de quien conoce su propia mesa.",
					new Line[] {
						N("Tomas Reed e a terceira geracao do pub de Dollet. A comissao escolheu a cidade pela sala, e ele se inscreveu para o nome do pub ir junto.", "Tomas Reed is the third generation of the Dollet pub. The committee chose the city for the room, and he signed up so the pub's name would go with it.", "Tomas Reed es la tercera generación del pub de Dollet. La comisión eligió la ciudad por la sala, y él se inscribió para que el nombre del pub fuera junto."),
						N("Joga calmo, no proprio territorio. Nao e soldado e nao finge ser.", "He plays calm, on his own ground. He isn't a soldier and he doesn't pretend to be.", "Juega tranquilo, en su propio terreno. No es soldado y no finge serlo."),
						N("Chubby Chocobo e o premio fixo. Tres pontos na rodada que fecha o mata-mata.", "Chubby Chocobo is the fixed prize. Three points in the round that closes the knockout.", "Chubby Chocobo es el premio fijo. Tres puntos en la ronda que cierra el mata-mata.")
					},
					new Line[] {
						N("Dollet, rodada decisiva, Grupo do Pub. Tomas Reed contra {name}.", "Dollet, decisive round, Pub Group. Tomas Reed against {name}.", "Dollet, ronda decisiva, Grupo del Pub. Tomas Reed contra {name}."),
						N("Plus defensivo. Tres pontos. Classificam dois, e eles nao caem no mesmo lado do mata-mata.", "Defensive Plus. Three points. Two qualify, and they don't fall on the same side of the knockout.", "Plus defensivo. Tres puntos. Clasifican dos, y no caen en el mismo lado del mata-mata.")
					},
					say("npc_pub_dollet", "Tomas Reed", "Tomas Reed", "Tomas Reed",
						"Partida tranquila, regra dura. Se ganhar, o Chubby sai da parede.",
						"A quiet match, a hard rule. Win, and Chubby comes off the wall.",
						"Partida tranquila, regla dura. Si ganas, Chubby sale de la pared."),
					say("npc_pub_dollet", "Tomas Reed", "Tomas Reed", "Tomas Reed",
						"Servido. O Chubby vai com voce. A Enne, la fora, nao tem o meu humor.",
						"Served. Chubby goes with you. Enne, outside, doesn't share my mood.",
						"Servido. Chubby se va contigo. Enne, afuera, no tiene mi humor."),
					lost("npc_pub_dollet", "Tomas Reed", "Tomas Reed", "Tomas Reed",
						"A casa fica com esta. A proxima eu ainda ponho na mesma mesa.",
						"The house keeps this one. The next one stays on the same table.",
						"La casa se queda con esta. La proxima sigue en la misma mesa."),
					new Line[] {
						N("Tres pontos no pub. A vaga de {name} ainda depende dos outros dois jogos.", "Three points at the pub. {name}'s spot still depends on the other two matches.", "Tres puntos en el pub. La plaza de {name} todavía depende de los otros dos partidos."),
						Q("Nao bebe a vitoria. A Doss joga na praca como quem defende o endereco.", "Don't drink the win. Doss plays in the square like someone defending the address.", "No te bebas la victoria. Doss juega en la plaza como quien defiende la dirección.")
					},
					new Line[] {
						N("O pub nao deu o ponto. Revanche contra o Tomas. Rodada decisiva sem numero e so conversa.", "The pub didn't give up the point. Rematch against Tomas. A decisive round without a number is only talk.", "El pub no dio el punto. Revancha contra Tomas. Una ronda decisiva sin número es solo charla.")
					}),
				duel("soldier_dollet", "npc_soldado_dollet", "Enne Doss", "Enne Doss", "Enne Doss", 5, 7,
					Options.AIType.BALANCED, Ruleset.ELEMENT, 81, true,
					"Rodada decisiva", "Decisive round", "Ronda decisiva",
					"Equilibrada. Elemental, orgulho da praca.",
					"Balanced. Elemental, proud of the square.",
					"Equilibrada. Elemental, orgullo de la plaza.",
					new Line[] {
						N("Enne Doss, cabo da guarnicao de Dollet. Joga na praca depois do turno e se inscreveu quando a fase veio para a cidade.", "Enne Doss, a corporal in the Dollet garrison. She plays in the square after her shift and signed up when the phase came to the city.", "Enne Doss, cabo de la guarnición de Dollet. Juega en la plaza después del turno y se inscribió cuando la fase llegó a la ciudad."),
						N("Quer que a praca tenha um nome no corte. Justa, e teimosa com a propria mao.", "She wants the square to have a name in the cutoff. Fair, and stubborn with her own hand.", "Quiere que la plaza tenga un nombre en el corte. Justa, y terca con su propia mano."),
						N("MiniMog e o premio fixo. Nao e o Holt nem a Quill.", "MiniMog is the fixed prize. It isn't Holt and it isn't Quill.", "MiniMog es el premio fijo. No es Holt ni es Quill.")
					},
					new Line[] {
						N("Rodada decisiva. Enne Doss contra {name}, com a praca do lado de fora.", "Decisive round. Enne Doss against {name}, with the square outside.", "Ronda decisiva. Enne Doss contra {name}, con la plaza afuera."),
						N("Elemental. Tres pontos. MiniMog so na primeira vitoria, fora da escolha comum.", "Elemental. Three points. MiniMog only on the first win, outside the common choice.", "Elemental. Tres puntos. MiniMog solo en la primera victoria, fuera de la elección común.")
					},
					say("npc_soldado_dollet", "Enne Doss", "Enne Doss", "Enne Doss",
						"A praca e o nosso endereco, nao uma trincheira. O MiniMog so sai numa vitoria limpa.",
						"The square is our address, not a trench. MiniMog only leaves on a clean win.",
						"La plaza es nuestra direccion, no una trinchera. MiniMog solo sale con una victoria limpia."),
					say("npc_soldado_dollet", "Enne Doss", "Enne Doss", "Enne Doss",
						"Pode descer. O MiniMog vai com voce. A tabela que decida o resto.",
						"Head down. MiniMog goes with you. Let the table decide the rest.",
						"Puedes bajar. MiniMog se va contigo. Que la tabla decida el resto."),
					lost("npc_soldado_dollet", "Enne Doss", "Enne Doss", "Enne Doss",
						"A praca continua no lugar. Arruma a mao e volta.",
						"The square is still where it was. Fix your hand and come back.",
						"La plaza sigue en su sitio. Arregla la mano y vuelve."),
					new Line[] {
						N("{name} deixa a Doss sem ponto neste confronto. A ultima mesa em Dollet e a Rainha.", "{name} leaves Doss without a point in this match. The last table in Dollet is the Queen.", "{name} deja a Doss sin punto en este enfrentamiento. La última mesa en Dollet es la Reina."),
						Q("Se os dois passarem, voces se separam no mata-mata. Nao facilite para 'resolver logo'.", "If you both go through, you split in the knockout. Don't make it easy just to 'get it over with'.", "Si los dos pasan, se separan en el mata-mata. No lo facilites para 'resolverlo ya'.")
					},
					new Line[] {
						N("Ponto da Doss nao virou seu. Revanche. O corte dos dois primeiros usa so vitoria.", "Doss's point didn't become yours. Rematch. The top-two cutoff uses wins only.", "El punto de Doss no se volvió tuyo. Revancha. El corte de los dos primeros usa solo la victoria.")
					}),
				duel("queen_dollet", "card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas", 6, 8,
					Options.AIType.OFFENSIVE, Ruleset.ELEMENT, 90, true,
					"Rodada decisiva", "Decisive round", "Ronda decisiva",
					"Ofensiva. Ela joga para ser primeira, nao para sobreviver.",
					"Offensive. She plays to finish first, not to survive.",
					"Ofensiva. Juega para quedar primera, no para sobrevivir.",
					new Line[] {
						N("A Rainha confirma em Dollet a entrada que trouxe de Deling. Mesmo grupo que {name}, ultima vez antes do mata-mata.", "The Queen confirms in Dollet the entry she brought from Deling. Same group as {name}, last time before the knockout.", "La Reina confirma en Dollet la entrada que trajo de Deling. Mismo grupo que {name}, última vez antes del mata-mata."),
						N("Se os dois terminarem no topo, o primeiro vai as quartas em FH e o segundo ao outro quarto.", "If you both finish at the top, first goes to the quarters at FH and second to the other quarter.", "Si los dos terminan arriba, el primero va a los cuartos en FH y el segundo al otro cuarto."),
						N("Diablos e o premio fixo deste encontro. A rivalidade ja tem nome.", "Diablos is this meeting's fixed prize. The rivalry already has a name.", "Diablos es el premio fijo de este encuentro. La rivalidad ya tiene nombre.")
					},
					new Line[] {
						N("Dollet, ultima mesa do grupo. A Rainha das Cartas contra {name}.", "Dollet, the group's last table. The Queen of Cards against {name}.", "Dollet, última mesa del grupo. La Reina de las Cartas contra {name}."),
						N("Elemental ofensivo. Tres pontos. Diablos na primeira vitoria. O primeiro lugar escolhe o lado de FH.", "Offensive Elemental. Three points. Diablos on the first win. First place chooses the FH side.", "Elemental ofensivo. Tres puntos. Diablos en la primera victoria. El primer lugar elige el lado de FH.")
					},
					say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"Parei no pub porque a fase e aqui. O Diablos pode mudar de mesa. O lado do mata-mata, tambem.",
						"I stopped at the pub because the phase is here. Diablos may change tables. So may the side of the knockout.",
						"Pare en el pub porque la fase es aqui. Diablos puede cambiar de mesa. El lado del mata-mata, tambien."),
					say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"Leva o Diablos. Se a tabela te puser na frente, eu vou ao outro quarto. Nao acabou.",
						"Take Diablos. If the table puts you first, I go to the other quarter. This isn't over.",
						"Llevate a Diablos. Si la tabla te pone delante, yo voy al otro cuarto. No se acabo."),
					lost("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"O Diablos prefere ficar. Primeiro lugar nao se herda.",
						"Diablos would rather stay. First place isn't inherited.",
						"Diablos prefiere quedarse. El primer puesto no se hereda."),
					new Line[] {
						N("Olhe o Grupo do Pub. Dois passam, lados opostos. {name} joga as quartas em Fisherman's Horizon se ficar no corte.", "Look at the Pub Group. Two go through, opposite sides. {name} plays the quarters at Fisherman's Horizon if the cutoff holds.", "Mira el Grupo del Pub. Pasan dos, lados opuestos. {name} juega los cuartos en Fisherman's Horizon si queda en el corte."),
						Q("FH e comunidade e agua. A Rainha, se for segunda, nao senta na sua chave de la. Ainda tem a cabeca de chave, separada.", "FH is community and water. The Queen, if she's second, doesn't sit in your bracket there. There's still a separate seed match.", "FH es comunidad y agua. La Reina, si queda segunda, no se sienta en tu llave de allá. Todavía está la cabeza de llave, aparte.")
					},
					new Line[] {
						N("A Rainha segurou Dollet nesta mesa. Revanche. Sem os tres pontos, o mata-mata nao tem o seu nome.", "The Queen held Dollet at this table. Rematch. Without the three points, the knockout doesn't have your name.", "La Reina retuvo Dollet en esta mesa. Revancha. Sin los tres puntos, el mata-mata no tiene tu nombre.")
					})
			});
	}

	private static Region fh() {
		return region("fh", "chegada_fh", "mesa_fh.png", Kind.GROUP, 1,
			new String[][] {{"flo", "pescador"}, {"flo", "shumi"}, {"shumi", "pescador"}},
			"FH - Quartas", "FH - Quarterfinals", "FH - Cuartos",
			new Line[] {
				N("Dollet fechou o corte. {name} entrou. O primeiro lugar deste lado joga as quartas aqui.", "Dollet closed the cutoff. {name} is in. First place on this side plays the quarters here.", "Dollet cerró el corte. {name} entró. El primer lugar de este lado juega los cuartos aquí."),
				N("Fisherman's Horizon sedia porque a plataforma cabe o publico sem expulsar o cais. E a fase da casa, nao um cerco.", "Fisherman's Horizon hosts it because the platform fits the crowd without pushing the dock out. It's the home phase, not a siege.", "Fisherman's Horizon la sede porque la plataforma cabe al público sin echar al muelle. Es la fase de casa, no un cerco."),
				Q("Antes do grupo tem uma partida de cabeca de chave contra a Rainha. Nao da ponto e nao elimina nenhum dos dois.", "Before the group there's a seed match against the Queen. It scores no point and eliminates neither of you.", "Antes del grupo hay una partida de cabeza de llave contra la Reina. No da punto y no elimina a ninguno de los dos."),
				N("Depois, o Grupo das Quartas: voce, Nash Pell, o representante Shumi e Dobe e Flo. So o primeiro vai a semifinal.", "Then the Quarter Group: you, Nash Pell, the Shumi representative, and Dobe and Flo. Only first goes to the semifinal.", "Después, el Grupo de los Cuartos: tú, Nash Pell, el representante Shumi y Dobe y Flo. Solo el primero va a la semifinal."),
				say("dobe_flo", "Flo", "Flo", "Flo",
					"A Horizon joga junto. Dobe e eu somos uma ficha. Pode sentar, {name}. A casa nao vai facilitar.",
					"Horizon plays together. Dobe and I are one entry. Sit down, {name}. The house won't make it easy.",
					"Horizon juega junta. Dobe y yo somos una ficha. Puedes sentarte, {name}. La casa no lo pondra facil.")
			},
			new Duel[] {
				duel("queen_fh", "card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas", 7, 7,
					Options.AIType.OFFENSIVE, Ruleset.FINAL, 98, false,
					"Cabeca de chave", "Seed match", "Cabeza de llave",
					"Todas as regras. Nao vale ponto de grupo.",
					"Every rule on. It does not score group points.",
					"Todas las reglas. No suma puntos de grupo.",
					new Line[] {
						N("A comissao marcou esta mesa no cais, antes das quartas. Quem ganha entra como cabeca do proprio quarto.", "The committee set this table on the dock, before the quarters. The winner enters as the seed of their own quarter.", "La comisión marcó esta mesa en el muelle, antes de los cuartos. Quien gana entra como cabeza de su propio cuarto."),
						N("A Rainha segue no outro lado mesmo se perder. {name} segue nas quartas daqui mesmo se perder. Por isso existe a revanche, e por isso nao ha ponto.", "The Queen stays on the other side even if she loses. {name} stays in the quarters here even on a loss. That's why the rematch exists, and why there is no point.", "La Reina sigue en el otro lado aunque pierda. {name} sigue en los cuartos de aquí aunque pierda. Por eso existe la revancha, y por eso no hay punto."),
						N("Doomtrain e o premio fixo. Nao entra na mao e nao substitui a carta que voce escolher.", "Doomtrain is the fixed prize. It doesn't enter the hand and it doesn't replace the card you choose.", "Doomtrain es el premio fijo. No entra en la mano y no sustituye la carta que eliges.")
					},
					new Line[] {
						N("Fisherman's Horizon. Partida de cabeca de chave, fora da tabela. A Rainha contra {name}.", "Fisherman's Horizon. Seed match, off the table. The Queen against {name}.", "Fisherman's Horizon. Partida de cabeza de llave, fuera de la tabla. La Reina contra {name}."),
						N("Same, Plus, Combo, Elemental e Morte subita. Ninguem e eliminado. Doomtrain so na primeira vitoria.", "Same, Plus, Combo, Elemental and Sudden Death. Nobody is eliminated. Doomtrain only on the first win.", "Same, Plus, Combo, Elemental y Muerte súbita. Nadie queda eliminado. Doomtrain solo en la primera victoria.")
					},
					say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"Isto nao me tira do outro quarto e nao te tira do seu. Ainda assim eu quero a cabeca, {name}.",
						"This doesn't take me off the other quarter, or you off yours. I still want the seed, {name}.",
						"Esto no me saca del otro cuarto ni a ti del tuyo. Aun asi quiero la cabeza, {name}."),
					say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"Leva o Doomtrain. Eu vou jogar o meu quarto. Se os dois ganharmos o lado, a final tem nome.",
						"Take Doomtrain. I'll play my quarter. If we both win our side, the final has a name.",
						"Llevate a Doomtrain. Yo juego mi cuarto. Si los dos ganamos nuestro lado, la final tiene nombre."),
					lost("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"A cabeca fica. O trem nao levou ninguem embora do torneio.",
						"The seed stays. The train didn't take anyone out of the tournament.",
						"La cabeza se queda. El tren no se llevo a nadie del torneo."),
					new Line[] {
						N("Cabeca de chave com {name}. A Rainha permanece no outro quarto. Agora a tabela das quartas: so o primeiro passa.", "Seed to {name}. The Queen stays in the other quarter. Now the quarter table: only first goes through.", "Cabeza de llave para {name}. La Reina permanece en el otro cuarto. Ahora la tabla de los cuartos: solo el primero pasa."),
						Q("Nash, o Shumi e a casa do prefeito. Um grupo pequeno e uma vaga so.", "Nash, the Shumi and the mayor's house. A small group and one spot.", "Nash, el Shumi y la casa del alcalde. Un grupo pequeño y una sola plaza.")
					},
					new Line[] {
						N("Cabeca de chave sem vitoria nao se escreve. Revanche na mesma mesa. Ninguem sai do torneio por este resultado.", "A seed without a win doesn't get written down. Rematch at the same table. Nobody leaves the tournament on this result.", "Una cabeza de llave sin victoria no se escribe. Revancha en la misma mesa. Nadie sale del torneo por este resultado.")
					}),
				duel("pescador", "npc_pescador", "Nash Pell", "Nash Pell", "Nash Pell", 5, 6,
					Options.AIType.BALANCED, Ruleset.PLUS, 91, true,
					"Quartas de final", "Quarterfinal", "Cuartos de final",
					"Plus de cais. Calmo, e ele nao solta cedo.",
					"Dockside Plus. Calm, and he doesn't let go early.",
					"Plus de muelle. Tranquilo, y no suelta pronto.",
					new Line[] {
						N("Nash Pell pesca na Horizon e se inscreveu quando a fase veio para a plataforma.", "Nash Pell fishes at Horizon and signed up when the phase came to the platform.", "Nash Pell pesca en Horizon y se inscribió cuando la fase llegó a la plataforma."),
						N("Compara carta com peixe sem transformar isso em guerra. Quer o primeiro lugar do grupo da casa.", "He compares a card to a fish without turning it into a war. He wants first place in the home group.", "Compara la carta con un pez sin convertir eso en una guerra. Quiere el primer lugar del grupo de casa."),
						N("Leviathan e o premio fixo. Tres pontos nas quartas.", "Leviathan is the fixed prize. Three points in the quarters.", "Leviathan es el premio fijo. Tres puntos en los cuartos.")
					},
					new Line[] {
						N("Quartas em Fisherman's Horizon. Nash Pell contra {name}.", "Quarters at Fisherman's Horizon. Nash Pell against {name}.", "Cuartos en Fisherman's Horizon. Nash Pell contra {name}."),
						N("Plus. Tres pontos. Neste grupo so o primeiro vai a Esthar. Leviathan fora da mao, na primeira vitoria.", "Plus. Three points. In this group only first goes to Esthar. Leviathan outside the hand, on the first win.", "Plus. Tres puntos. En este grupo solo el primero va a Esthar. Leviathan fuera de la mano, en la primera victoria.")
					},
					say("npc_pescador", "Nash Pell", "Nash Pell", "Nash Pell",
						"Linha curta. O Leviathan so sai do anzol se voce merecer o ponto tambem.",
						"Short line. Leviathan only leaves the hook if you earn the point too.",
						"Linea corta. Leviathan solo sale del anzuelo si tambien te ganas el punto."),
					say("npc_pescador", "Nash Pell", "Nash Pell", "Nash Pell",
						"Fisgou. Os Shumi jogam a seguir, na mesma agua. Nao trata o Muum como eu.",
						"Hooked. The Shumi play next, on the same water. Don't treat Muum the way you treat me.",
						"Picaste. Los Shumi juegan despues, en la misma agua. No trates a Muum como me tratas a mi."),
					lost("npc_pescador", "Nash Pell", "Nash Pell", "Nash Pell",
						"Escapou. Isca de novo. O grupo nao espera quase.",
						"It got away. Bait it again. The group doesn't wait on almost.",
						"Se escapo. Ceba otra vez. El grupo no espera un casi."),
					new Line[] {
						N("Tres pontos nas quartas. O representante Shumi e o proximo.", "Three points in the quarters. The Shumi representative is next.", "Tres puntos en los cuartos. El representante Shumi es el siguiente."),
						Q("O Muum joga devagar de proposito. Nao confunde isso com fraqueza.", "Muum plays slowly on purpose. Don't confuse that with weakness.", "Muum juega despacio a propósito. No confundas eso con debilidad.")
					},
					new Line[] {
						N("Nash segurou o ponto. Revanche. Nas quartas, so vitoria conta, e so o primeiro lugar segue.", "Nash held the point. Rematch. In the quarters only a win counts, and only first place goes on.", "Nash retuvo el punto. Revancha. En los cuartos solo cuenta la victoria, y solo el primer lugar sigue.")
					}),
				duel("shumi", "shumi", "Anciao Muum", "Elder Muum", "Anciano Muum", 6, 7,
					Options.AIType.BALANCED, Ruleset.ELEMENT, 89, true,
					"Quartas de final", "Quarterfinal", "Cuartos de final",
					"Lento e Elemental. Cada carta e uma forma.",
					"Slow and Elemental. Each card is a shape.",
					"Lento y Elemental. Cada carta es una forma.",
					new Line[] {
						N("O anciao Muum veio da vila Shumi, perto da agua, como representante. Uma ficha, um nome.", "Elder Muum came from the Shumi village, near the water, as their representative. One entry, one name.", "El anciano Muum vino de la aldea Shumi, cerca del agua, como representante. Una ficha, un nombre."),
						N("A vila pediu presenca. Ele ficou porque quer a forma certa e o primeiro lugar, nessa ordem.", "The village asked for a presence. He stayed because he wants the right shape and first place, in that order.", "La aldea pidió presencia. Él se quedó porque quiere la forma correcta y el primer lugar, en ese orden."),
						N("Carbuncle e o premio fixo. Nao e a Flo e nao e o Nash.", "Carbuncle is the fixed prize. It isn't Flo and it isn't Nash.", "Carbuncle es el premio fijo. No es Flo y no es Nash.")
					},
					new Line[] {
						N("Quartas, Horizon. Anciao Muum contra {name}.", "Quarters, Horizon. Elder Muum against {name}.", "Cuartos, Horizon. Anciano Muum contra {name}."),
						N("Elemental, ritmo lento. Tres pontos. Carbuncle na primeira vitoria, separado da mao.", "Elemental, a slow pace. Three points. Carbuncle on the first win, apart from the hand.", "Elemental, ritmo lento. Tres puntos. Carbuncle en la primera victoria, separado de la mano.")
					},
					say("shumi", "Anciao Muum", "Elder Muum", "Anciano Muum",
						"A vila esculpe devagar. Eu jogo do mesmo jeito. O Carbuncle ficou a forma mais limpa.",
						"The village carves slowly. I play the same way. Carbuncle came out the cleanest shape.",
						"La aldea talla despacio. Yo juego igual. Carbuncle quedo la forma mas limpia."),
					say("shumi", "Anciao Muum", "Elder Muum", "Anciano Muum",
						"Boa forma. O Carbuncle muda de mao. A Flo ainda e a casa.",
						"Good shape. Carbuncle changes hands. Flo is still the house.",
						"Buena forma. Carbuncle cambia de mano. Flo sigue siendo la casa."),
					lost("shumi", "Anciao Muum", "Elder Muum", "Anciano Muum",
						"Ainda cru. Trabalha de novo. Eu espero.",
						"Still rough. Work it again. I'll wait.",
						"Todavia esta crudo. Trabájalo otra vez. Yo espero."),
					new Line[] {
						N("{name} passa o Muum na tabela. A ultima das quartas e Dobe e Flo, uma ficha so.", "{name} gets past Muum on the table. The last of the quarters is Dobe and Flo, one entry.", "{name} pasa a Muum en la tabla. La última de los cuartos es Dobe y Flo, una sola ficha."),
						Q("Eles jogam como a casa. Nao como dois contra um. Uma entrada.", "They play as the house. Not as two against one. One entry.", "Ellos juegan como la casa. No como dos contra uno. Una entrada.")
					},
					new Line[] {
						N("O Muum nao cedeu o ponto. Revanche, no tempo dele. A vaga da semi e um primeiro lugar inteiro.", "Muum didn't give up the point. Rematch, on his time. The semi spot is a whole first place.", "Muum no cedió el punto. Revancha, a su tiempo. La plaza de la semi es un primer lugar entero.")
					}),
				duel("flo", "dobe_flo", "Dobe e Flo", "Dobe and Flo", "Dobe y Flo", 6, 8,
					Options.AIType.OFFENSIVE, Ruleset.ELEMENT, 96, true,
					"Quartas de final", "Quarterfinal", "Cuartos de final",
					"A casa joga ofensiva, como uma ficha so.",
					"The house plays offensive, as one entry.",
					"La casa juega ofensiva, como una sola ficha.",
					new Line[] {
						N("Dobe, o prefeito, e Flo entram como uma so ficha da Horizon. A comunidade senta junto, a mao e uma.", "Dobe, the mayor, and Flo enter as one Horizon entry. The community sits together. The hand is one.", "Dobe, el alcalde, y Flo entran como una sola ficha de Horizon. La comunidad se sienta junta. La mano es una."),
						N("Querem que as quartas parecam a plataforma, e querem o primeiro lugar. Nao dao a vaga de presente.", "They want the quarters to feel like the platform, and they want first place. They don't give the spot away.", "Quieren que los cuartos parezcan la plataforma, y quieren el primer lugar. No regalan la plaza."),
						N("Phoenix e o premio fixo. Vencer aqui, com o resto da tabela, manda {name} a semifinal.", "Phoenix is the fixed prize. Winning here, with the rest of the table, sends {name} to the semifinal.", "Phoenix es el premio fijo. Ganar aquí, con el resto de la tabla, manda a {name} a la semifinal.")
					},
					new Line[] {
						N("Final do grupo das quartas. Dobe e Flo contra {name}.", "End of the quarter group. Dobe and Flo against {name}.", "Final del grupo de los cuartos. Dobe y Flo contra {name}."),
						N("Elemental ofensivo. Tres pontos. So o primeiro deste grupo vai a Esthar. Phoenix na primeira vitoria.", "Offensive Elemental. Three points. Only first in this group goes to Esthar. Phoenix on the first win.", "Elemental ofensivo. Tres puntos. Solo el primero de este grupo va a Esthar. Phoenix en la primera victoria.")
					},
					say("dobe_flo", "Flo", "Flo", "Flo",
						"O prefeito e eu jogamos como um. A Phoenix esta em casa. Aguenta a Horizon, {name}.",
						"The mayor and I play as one. Phoenix is home. Handle Horizon, {name}.",
						"El alcalde y yo jugamos como uno. Phoenix esta en casa. Aguanta Horizon, {name}."),
					say("dobe_flo", "Flo", "Flo", "Flo",
						"A plataforma viu. A Phoenix e sua. Vai, se a tabela deixar. A casa aplaude mesmo assim.",
						"The platform saw it. Phoenix is yours. Go, if the table allows. The house claps either way.",
						"La plataforma lo vio. Phoenix es tuya. Ve, si la tabla lo permite. La casa aplaude igual."),
					lost("dobe_flo", "Flo", "Flo", "Flo",
						"A casa ganhou esta. Bebe agua e volta. A vaga nao sai no quase.",
						"The house won this one. Drink some water and come back. The spot doesn't leave on almost.",
						"La casa gano esta. Bebe agua y vuelve. La plaza no sale con un casi."),
					new Line[] {
						N("Quartas encerradas para {name}. O primeiro lugar vai a semifinal em Esthar. O outro grupo eu conto la, sem inventar tabela.", "Quarters closed for {name}. First place goes to the semifinal in Esthar. The other group I'll report there, without inventing a table.", "Cuartos cerrados para {name}. El primer lugar va a la semifinal en Esthar. El otro grupo lo cuento allá, sin inventar tabla."),
						Q("Esthar e luz fria e laboratorio. O Seifer chegou naquele lado. Eu vi o nome.", "Esthar is cold light and a laboratory. Seifer arrived on that side. I saw the name.", "Esthar es luz fría y laboratorio. Seifer llegó a ese lado. Yo vi el nombre.")
					},
					new Line[] {
						N("A casa segurou os tres pontos. Revanche contra Dobe e Flo. Sem isso, a semi nao tem {name}.", "The house held the three points. Rematch against Dobe and Flo. Without that, the semi doesn't have {name}.", "La casa retuvo los tres puntos. Revancha contra Dobe y Flo. Sin eso, la semi no tiene a {name}.")
					})
			});
	}

	private static Region esthar() {
		return region("esthar", "chegada_esthar", "mesa_esthar.png", Kind.GROUP, 1,
			new String[][] {{"seifer", "cientista"}, {"seifer", "odine"}, {"odine", "cientista"}},
			"Esthar - Semifinal", "Esthar - Semifinal", "Esthar - Semifinal",
			new Line[] {
				N("{name} terminou em primeiro nas quartas de FH. A semi deste lado e em Esthar.", "{name} finished first in the FH quarters. This side's semi is in Esthar.", "{name} terminó {primero|primera} en los cuartos de FH. La semi de este lado es en Esthar."),
				N("A cidade sedia porque o laboratorio tem sala, luz e publico. Nao porque haja uma crise. O torneio e o assunto.", "The city hosts it because the lab has a room, light and a crowd. Not because there's a crisis. The tournament is the subject.", "La ciudad la sede porque el laboratorio tiene sala, luz y público. No porque haya una crisis. El torneo es el asunto."),
				Q("O Seifer subiu a escada dele desde o Garden. Esta neste grupo. A Rainha joga o outro. Eu nao finjo ter a tabela de la.", "Seifer climbed his own ladder from Garden. He's in this group. The Queen plays the other one. I won't pretend I have that table.", "Seifer subió su escalera desde el Garden. Está en este grupo. La Reina juega el otro. Yo no finjo tener la tabla de allá."),
				N("Grupo da semifinal: {name}, Ivo Lenn, Odine e Seifer. Vitoria vale 3. So o primeiro vai a grande final.", "Semifinal group: {name}, Ivo Lenn, Odine and Seifer. A win is worth 3. Only first goes to the grand final.", "Grupo de la semifinal: {name}, Ivo Lenn, Odine y Seifer. La victoria vale 3. Solo el primero va a la gran final."),
				say("seifer", "Seifer", "Seifer", "Seifer",
					"Olha quem sobreviveu as tabelas. {name}, eu nao vim aplaudir. Vim sentar por ultimo.",
					"Look who survived the tables. {name}, I didn't come to clap. I came to sit last.",
					"Mira quien sobrevivio a las tablas. {name}, no vine a aplaudir. Vine a sentarme el ultimo.")
			},
			new Duel[] {
				duel("cientista", "npc_cientista_esthar", "Ivo Lenn", "Ivo Lenn", "Ivo Lenn", 6, 7,
					Options.AIType.OFFENSIVE, Ruleset.ELEMENT, 92, true,
					"Semifinal", "Semifinal", "Semifinal",
					"Ofensivo, de laboratorio. Elemental anotado.",
					"Offensive, from the lab. Elemental, written down.",
					"Ofensivo, de laboratorio. Elemental anotado.",
					new Line[] {
						N("Ivo Lenn e assistente de Odine. Entrou porque o laboratorio pediu um jogador. Ficou porque quer a vitoria no proprio nome.", "Ivo Lenn is Odine's assistant. He entered because the lab asked for a player. He stayed because he wants the win in his own name.", "Ivo Lenn es asistente de Odine. Entró porque el laboratorio pidió un jugador. Se quedó porque quiere la victoria en su propio nombre."),
						N("Nao fala como o chefe. Anota, joga para a frente, e nao pede licenca na mesa.", "He doesn't talk like his boss. He takes notes, plays forward, and doesn't ask permission at the table.", "No habla como el jefe. Anota, juega hacia adelante, y no pide permiso en la mesa."),
						N("Odin e o premio fixo. Tres pontos na semi.", "Odin is the fixed prize. Three points in the semi.", "Odin es el premio fijo. Tres puntos en la semi.")
					},
					new Line[] {
						N("Esthar, grupo da semifinal. Ivo Lenn contra {name}.", "Esthar, semifinal group. Ivo Lenn against {name}.", "Esthar, grupo de la semifinal. Ivo Lenn contra {name}."),
						N("Elemental ofensivo. Tres pontos. So o primeiro deste grupo vai a final no Garden. Odin fora da mao.", "Offensive Elemental. Three points. Only first in this group goes to the final at Garden. Odin outside the hand.", "Elemental ofensivo. Tres puntos. Solo el primero de este grupo va a la final en el Garden. Odin fuera de la mano.")
					},
					say("npc_cientista_esthar", "Ivo Lenn", "Ivo Lenn", "Ivo Lenn",
						"O Odine pediu teste. Eu pedi a cadeira. Se ganhar, o Odin sai com voce. Se perder, repete.",
						"Odine asked for a test. I asked for the chair. Win, and Odin leaves with you. Lose, and it runs again.",
						"Odine pidio una prueba. Yo pedi la silla. Si ganas, Odin se va contigo. Si pierdes, se repite."),
					say("npc_cientista_esthar", "Ivo Lenn", "Ivo Lenn", "Ivo Lenn",
						"Anotado no meu nome, nao no dele. O Odine ja quer o proximo. Boa sorte com isso.",
						"Logged under my name, not his. Odine already wants the next one. Good luck with that.",
						"Anotado a mi nombre, no al suyo. Odine ya quiere el siguiente. Suerte con eso."),
					lost("npc_cientista_esthar", "Ivo Lenn", "Ivo Lenn", "Ivo Lenn",
						"Nao fechou. A gente recomeca. Eu nao empresto o ponto.",
						"It didn't close. We start over. I don't lend the point.",
						"No cerro. Empezamos de nuevo. Yo no presto el punto."),
					new Line[] {
						N("Tres pontos na semi. Odine e o proximo, e ele joga por conta propria.", "Three points in the semi. Odine is next, and he plays for himself.", "Tres puntos en la semi. Odine es el siguiente, y juega por su cuenta."),
						Q("Nao discute teoria com ele. Discute a mesa.", "Don't argue theory with him. Argue the table.", "No discutas teoría con él. Discute la mesa.")
					},
					new Line[] {
						N("Lenn segurou o ponto. Revanche. A semifinal nao classifica por quase.", "Lenn held the point. Rematch. The semifinal doesn't qualify on almost.", "Lenn retuvo el punto. Revancha. La semifinal no clasifica por un casi.")
					}),
				duel("odine", "odine", "Odine", "Odine", "Odine", 7, 7,
					Options.AIType.OFFENSIVE, Ruleset.FINAL, 99, true,
					"Semifinal", "Semifinal", "Semifinal",
					"Todas as regras. Orgulho curto, jogo longo.",
					"Every rule. Short pride, a long game.",
					"Todas las reglas. Orgullo corto, partida larga.",
					new Line[] {
						N("Odine entrou pelo titulo e pelos dados, nessa ordem quando a mesa aperta. Nao repete a fala do assistente.", "Odine entered for the title and the data, in that order when the table gets tight. He doesn't repeat the assistant's speech.", "Odine entró por el título y por los datos, en ese orden cuando la mesa aprieta. No repite el discurso del asistente."),
						N("Esthar e a casa dele. Isso nao lhe da ponto antes da carta.", "Esthar is his home. That doesn't give him a point before the card.", "Esthar es su casa. Eso no le da un punto antes de la carta."),
						N("Eden e o premio fixo. Primeira vitoria, nunca na escolha comum.", "Eden is the fixed prize. First win, never in the common choice.", "Eden es el premio fijo. Primera victoria, nunca en la elección común.")
					},
					new Line[] {
						N("Semifinal, Esthar. Odine contra {name}.", "Semifinal, Esthar. Odine against {name}.", "Semifinal, Esthar. Odine contra {name}."),
						N("Regras completas, com Morte subita se empatar. Tres pontos. Eden separada da mao.", "Full rules, with Sudden Death on a draw. Three points. Eden apart from the hand.", "Reglas completas, con Muerte súbita si hay empate. Tres puntos. Eden separada de la mano.")
					},
					say("odine", "Odine", "Odine", "Odine",
						"Eden ainda e teoria ate alguem me ganhar. Os dados podem esperar. O ponto, nao.",
						"Eden is still a theory until someone beats me. The data can wait. The point can't.",
						"Eden sigue siendo teoria hasta que alguien me gane. Los datos pueden esperar. El punto, no."),
					say("odine", "Odine", "Odine", "Odine",
						"Confirmado. Irritante. O Seifer e o ultimo deste grupo. Nao me peca para explica-lo.",
						"Confirmed. Irritating. Seifer is the last one in this group. Don't ask me to explain him.",
						"Confirmado. Irritante. Seifer es el ultimo de este grupo. No me pidas que lo explique."),
					lost("odine", "Odine", "Odine", "Odine",
						"Nao serve. Volta com uma mao melhor. Eu continuo aqui.",
						"Doesn't hold. Come back with a better hand. I'm still here.",
						"No sirve. Vuelve con una mano mejor. Yo sigo aqui."),
					new Line[] {
						N("{name} tira ponto de Odine. A ultima mesa da semi e Seifer.", "{name} takes a point off Odine. The semi's last table is Seifer.", "{name} le saca un punto a Odine. La última mesa de la semi es Seifer."),
						Q("Ele provoca desde o Garden. Deixa. A carta e que responde.", "He's been needling you since Garden. Let him. The card is what answers.", "Provoca desde el Garden. Déjalo. La carta es la que responde.")
					},
					new Line[] {
						N("Odine nao cedeu. Revanche com as mesmas regras. Primeiro lugar nao se completa sem este jogo.", "Odine didn't give it up. Rematch under the same rules. First place isn't complete without this match.", "Odine no cedió. Revancha con las mismas reglas. El primer lugar no se completa sin este partido.")
					}),
				duel("seifer", "seifer", "Seifer", "Seifer", "Seifer", 6, 7,
					Options.AIType.OFFENSIVE, Ruleset.FINAL, 109, true,
					"Semifinal", "Semifinal", "Semifinal",
					"Ofensivo e provocador. Todas as regras.",
					"Offensive and needling. Every rule on.",
					"Ofensivo y provocador. Todas las reglas.",
					new Line[] {
						N("Seifer Almasy, aluno de Balamb, rival de {name} desde a inscricao. Subiu a outra escada e caiu neste grupo.", "Seifer Almasy, a Balamb student, {name}'s rival since registration. He climbed the other ladder and landed in this group.", "Seifer Almasy, alumno de Balamb, rival de {name} desde la inscripción. Subió la otra escalera y cayó en este grupo."),
						N("Quer o titulo para dizer que a cadeira do meio e dele. Nao esta aqui a mando de ninguem.", "He wants the title so he can say the middle chair is his. He isn't here on anyone's orders.", "Quiere el título para decir que la silla del medio es suya. No está aquí por orden de nadie."),
						N("A carta Seifer e o premio fixo. Nao aparece na mao. Vencer este grupo manda {name} a final.", "The Seifer card is the fixed prize. It doesn't appear in the hand. Winning this group sends {name} to the final.", "La carta de Seifer es el premio fijo. No aparece en la mano. Ganar este grupo manda a {name} a la final.")
					},
					new Line[] {
						N("Esthar. Ultima mesa da semifinal. Seifer contra {name}.", "Esthar. Last table of the semifinal. Seifer against {name}.", "Esthar. Última mesa de la semifinal. Seifer contra {name}."),
						N("Regras completas. Tres pontos. Se a tabela fechar com {name} em primeiro, a grande final e no Garden. A carta dele so na primeira vitoria.", "Full rules. Three points. If the table closes with {name} in first, the grand final is at Garden. His card only on the first win.", "Reglas completas. Tres puntos. Si la tabla cierra con {name} {primero|primera}, la gran final es en el Garden. Su carta solo en la primera victoria.")
					},
					say("seifer", "Seifer", "Seifer", "Seifer",
						"{name}. {Sonhador|Sonhadora} ate aqui. Ultima do grupo, todas as regras. Minha carta so sai se eu cair.",
						"{name}. Dreamer, all the way here. Last one in the group, every rule on. My card only leaves if I go down.",
						"{name}. {Sonador|Sonadora}, hasta aqui. La ultima del grupo, con todas las reglas. Mi carta solo sale si yo caigo."),
					say("seifer", "Seifer", "Seifer", "Seifer",
						"...Leva. Nao se acostuma. Se a tabela te puser na final, eu vou estar na arquibancada. Calado, nao.",
						"...Take it. Don't get used to it. If the table puts you in the final, I'll be in the stands. Not quietly.",
						"...Llevatela. No te acostumbres. Si la tabla te pone en la final, estare en la grada. Callado, no."),
					lost("seifer", "Seifer", "Seifer", "Seifer",
						"Ainda sonhando? Levanta. A semi nao tem versao facil.",
						"Still dreaming? Get up. The semi doesn't have an easy version.",
						"¿Sigues sonando? Levantate. La semi no tiene version facil."),
					new Line[] {
						N("Semi deste lado encerrada. Se {name} for {o primeiro|a primeira}, a grande final e em Balamb Garden.", "This side's semi is closed. If {name} is first, the grand final is at Balamb Garden.", "La semi de este lado está cerrada. Si {name} es {el primero|la primera}, la gran final es en Balamb Garden."),
						N("A Rainha venceu o outro grupo. Isso eu vi na ata, e so digo o resultado: a final e contra ela. A tabela de la nao esta nesta sala.", "The Queen won the other group. I saw that in the record, and I'll only say the result: the final is against her. That table isn't in this room.", "La Reina ganó el otro grupo. Eso lo vi en el acta, y solo digo el resultado: la final es contra ella. La tabla de allá no está en esta sala."),
						Q("De volta ao Garden. Desta vez o salao e o seu, nao o meu.", "Back to Garden. This time the hall is yours, not mine.", "De vuelta al Garden. Esta vez el salón es tuyo, no el mío.")
					},
					new Line[] {
						N("Seifer segurou a semi nesta mesa. Revanche. O primeiro lugar, e so ele, vai a final.", "Seifer held the semi at this table. Rematch. First place, and only first place, goes to the final.", "Seifer retuvo la semi en esta mesa. Revancha. El primer lugar, y solo él, va a la final.")
					})
			});
	}

	private static Region fin() {
		return region("final", "final_historia", "mesa_balamb_garden.png", Kind.FINAL, 0, null,
			"Garden - Grande final", "Garden - Final", "Garden - Gran final",
			new Line[] {
				Q("{name}. Voce voltou {campeão|campeã} de fase, nao de discurso. A final e uma mesa.", "{name}. You came back a phase champion, not a speech. The final is one table.", "{name}. Volviste {campeón|campeona} de fase, no de discurso. La final es una mesa."),
				N("O Garden sedia a grande final porque a inscricao comecou neste corredor. O circulo fecha aqui.", "Garden hosts the grand final because registration started in this hall. The circle closes here.", "El Garden sede la gran final porque la inscripción empezó en este pasillo. El círculo se cierra aquí."),
				N("Uma partida. Derrota nao entrega o titulo: a revanche e na mesma final, na hora. O titulo so muda com a sua vitoria.", "One match. A loss doesn't hand over the title: the rematch is the same final, on the spot. The title changes only with your win.", "Una partida. La derrota no entrega el título: la revancha es en la misma final, al momento. El título solo cambia con tu victoria."),
				N("Do outro grupo, a ata diz que a Rainha venceu. Eu nao finjo o chaveamento que nao esta nesta sala. A adversaria, esta, esta.", "From the other group, the record says the Queen won. I won't fake a bracket that isn't in this room. The opponent, though, is here.", "Del otro grupo, el acta dice que la Reina ganó. Yo no finjo un cuadro que no está en esta sala. La adversaria, esa, sí está."),
				say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
					"De Balamb ao cais, e de volta. {name}, agora nao e grupo. E o titulo.",
					"From Balamb to the dock, and back. {name}, this isn't a group. It's the title.",
					"De Balamb al muelle, y de vuelta. {name}, ahora no es un grupo. Es el titulo."),
				Q("Eu fico atras da cadeira. Nilo chama. Voce joga.", "I'll stay behind the chair. Nilo calls it. You play.", "Yo me quedo detrás de la silla. Nilo llama. Tú juegas.")
			},
			new Duel[] {
				duel("queen_esthar", "card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas", 6, 7,
					Options.AIType.OFFENSIVE, Ruleset.FINAL, 97, false,
					"Grande final", "Grand final", "Gran final",
					"Todas as regras. A mao dela nao inclui o Bahamut.",
					"Every rule. Her hand does not include Bahamut.",
					"Todas las reglas. Su mano no incluye a Bahamut.",
					new Line[] {
						N("A Rainha das Cartas atravessou o porto, Deling, Dollet e a cabeca de chave em FH. Nenhum desses jogos era a final.", "The Queen of Cards crossed the harbor, Deling, Dollet and the seed match at FH. None of those games was the final.", "La Reina de las Cartas cruzó el puerto, Deling, Dollet y la cabeza de llave en FH. Ninguno de esos partidos era la final."),
						N("Ela quer o titulo de volta, em publico, no Garden. Trata {name} pelo nome, e pelo que a tabela ja mostrou.", "She wants the title back, in public, at Garden. She uses {name}'s name, and what the table has already shown.", "Ella quiere el título de vuelta, en público, en el Garden. Trata a {name} por el nombre, y por lo que la tabla ya mostró."),
						N("Bahamut e o premio fixo da primeira vitoria. Nao esta na mao. A carta escolhida vem alem dele, uma vez cada.", "Bahamut is the fixed prize of the first win. It isn't in the hand. The chosen card comes besides it, once each.", "Bahamut es el premio fijo de la primera victoria. No está en la mano. La carta elegida viene además de él, una vez cada una.")
					},
					new Line[] {
						N("Balamb Garden. Grande final do Torneio Mundial. A Rainha das Cartas contra {name}.", "Balamb Garden. Grand final of the World Tournament. The Queen of Cards against {name}.", "Balamb Garden. Gran final del Torneo Mundial. La Reina de las Cartas contra {name}."),
						N("Same, muro, Plus, Combo, Elemental e Morte subita. Em jogo: o titulo. Bahamut so se {name} vencer pela primeira vez.", "Same, wall, Plus, Combo, Elemental and Sudden Death. At stake: the title. Bahamut only if {name} wins for the first time.", "Same, muro, Plus, Combo, Elemental y Muerte súbita. En juego: el título. Bahamut solo si {name} gana por primera vez.")
					},
					say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"Sem grupo, sem cabeca de chave. {name}, se a mesa for sua, o Bahamut e o titulo saem juntos. Eu nao entrego os dois sorrindo.",
						"No group, no seed match. {name}, if the table is yours, Bahamut and the title leave together. I don't hand both over with a smile.",
						"Sin grupo, sin cabeza de llave. {name}, si la mesa es tuya, Bahamut y el titulo se van juntos. No entrego los dos sonriendo."),
					say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"Leva o Bahamut. O titulo eu digo em voz alta: hoje e de {name}.",
						"Take Bahamut. The title I'll say out loud: today it belongs to {name}.",
						"Llevate a Bahamut. El titulo lo digo en voz alta: hoy es de {name}."),
					lost("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"O titulo fica. A final nao tem versao de consolacao. Senta de novo.",
						"The title stays. The final has no consolation version. Sit down again.",
						"El titulo se queda. La final no tiene version de consuelo. Sientate otra vez."),
					new Line[] {
						N("A mesa acabou. Antes da cerimonia, {name} escolhe uma carta da mao. O Bahamut, se for a primeira vez, vem junto e separado.", "The table is done. Before the ceremony, {name} chooses one card from the hand. Bahamut, if it's the first time, comes along and apart.", "La mesa terminó. Antes de la ceremonia, {name} elige una carta de la mano. Bahamut, si es la primera vez, viene junto y separado."),
						Q("Escolhe com calma. O salao espera.", "Choose calmly. The hall is waiting.", "Elige con calma. El salón espera.")
					},
					new Line[] {
						N("A grande final continua aberta. A regra e revanche na mesma mesa. O titulo nao muda de maos numa derrota.", "The grand final stays open. The rule is a rematch at the same table. The title doesn't change hands on a loss.", "La gran final sigue abierta. La regla es revancha en la misma mesa. El título no cambia de manos en una derrota.")
					})
			});
	}

	private static Line N(String pt, String en, String es) {
		return new Line(true, "narrator", false, "Nilo Vann", "Nilo Vann", "Nilo Vann", pt, en, es);
	}

	private static Line Q(String pt, String en, String es) {
		return new Line(false, "squall", false, "Squall", "Squall", "Squall", pt, en, es);
	}

	private static Line say(String portrait, String npt, String nen, String nes, String pt, String en, String es) {
		return new Line(false, portrait, false, npt, nen, nes, pt, en, es);
	}

	private static Line lost(String portrait, String npt, String nen, String nes, String pt, String en, String es) {
		return new Line(false, portrait, true, npt, nen, nes, pt, en, es);
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
			rolePt, roleEn, roleEs, stylePt, styleEn, styleEs, bio, open, before, win, lose, afterWin, afterLose);
	}
}
