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
				"Álbum curto demais pra sentar. Toma cinco cartas de nível 1. Na primeira mesa a mão é aberta. Depois disso, Same e Combo.",
				"Album's too thin to sit. Take five level 1 cards. First table is Open. After that, Same and Combo.",
				"El álbum no alcanza para sentarte. Toma cinco cartas de nivel 1. En la primera mesa la mano es abierta. Después, Same y Combo.")
		};
	}

	static Line[] ceremony() {
		return new Line[] {
			N("{name}. O título fica com {o aluno|a aluna} de Balamb. {Campeão|Campeã} mundial de Triple Triad.",
				"{name}. The title goes to the Balamb student. World Triple Triad champion.",
				"{name}. El título se queda con {el alumno|la alumna} de Balamb. {Campeón|Campeona} mundial de Triple Triad."),
			Q("Você fechou a última casa. Eu só assisti. ...Whatever. O título é seu.",
				"You closed the last square. I only watched. ...Whatever. The title is yours.",
				"Cerraste la última casilla. Yo solo miré. ...Whatever. El título es tuyo."),
			say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
				"Morte súbita não me salvou, {name}. Leva o Bahamut. Quando quiser revanche, eu já conheço as regras.",
				"Sudden Death didn't save me, {name}. Take Bahamut. When you want a rematch, I already know the rules.",
				"Muerte súbita no me salvó, {name}. Llévate a Bahamut. Cuando quieras la revancha, ya conozco las reglas."),
			say("seifer", "Seifer", "Seifer", "Seifer",
				"...Não se acostuma, {name}. Same Wall te salvou hoje. Ano que vem eu trago outra mão.",
				"...Don't get used to it, {name}. Same Wall saved you today. Next year I bring a different hand.",
				"...No te acostumbres, {name}. Same Wall te salvó hoy. El año que viene traigo otra mano."),
			N("Está encerrado o Torneio Mundial! {name}, de Balamb Garden, é {o campeão|a campeã}!",
				"The World Tournament is closed! {name}, of Balamb Garden, is the champion!",
				"¡Queda cerrado el Torneo Mundial! ¡{name}, de Balamb Garden, es {el campeón|la campeona}!")
		};
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
				Q("{name}. Eu não me sento. Você joga. A primeira mesa é Open: os ranks dela ficam à vista. Depois, Same e Combo.",
					"{name}. I don't sit. You play. First table is Open: you can see her ranks. After that, Same and Combo.",
					"{name}. Yo no me siento. Tú juegas. La primera mesa es Open: ves sus rangos. Después, Same y Combo."),
				N("A seletiva é uma chave contínua. Quem vence enfrenta o próximo cabeça de chave. Quem perde repete o confronto, sem ser eliminado.",
					"The qualifier is a ladder. A win moves you to the next seed. A loss replays the same tie and doesn't eliminate you.",
					"La selectiva es una llave continua. Quien gana enfrenta al siguiente cabeza de serie. Quien pierde repite el cruce, sin quedar eliminado."),
				Q("O Seifer subiu pela outra chave. A sua passa pela Xu e termina na Quistis. Não solte um Plus que você não consegue fechar.",
					"Seifer took the other ladder. Yours goes through Xu and ends at Quistis. Don't drop a Plus you can't finish.",
					"Seifer subió por la otra llave. La tuya pasa por Xu y termina en Quistis. No sueltes un Plus que no puedas cerrar."),
				N("Quem vencer a final da seletiva entra no mundial. {name}, a primeira cadeira é a de Lina Faris.",
					"Whoever wins the qualifier final enters the world tournament. {name}, the first chair belongs to Lina Faris.",
					"Quien gane la final de la selectiva entra al mundial. {name}, la primera silla es la de Lina Faris.")
			},
			new Duel[] {
				duel("cadete", "npc_cadete", "Lina Faris", "Lina Faris", "Lina Faris", 1, 2,
					Options.AIType.DEFENSIVE, Ruleset.OPEN_BASIC, 0, false,
					"Inscrição", "Entry match", "Inscripción",
					"Mão aberta e jogo curto. Ela mostra as cartas.",
					"Open hand, short game. She shows her cards.",
					"Mano abierta y partida corta. Ella muestra las cartas.",
					new Line[] {
						N("Lina Faris, caloura de Balamb. Entrou porque a inscrição do Garden é aberta a quem estuda aqui.", "Lina Faris, a Balamb freshman. She entered because Garden registration is open to anyone who studies here.", "Lina Faris, estudiante de primer año de Balamb. Entró porque la inscripción del Garden está abierta a quien estudia aquí."),
						N("Quer provar que tem lugar no torneio, não só na sala de aula. Joga devagar e com a mão aberta.", "She wants to prove she belongs in the tournament, not just in class. She plays slowly, with an open hand.", "Quiere demostrar que tiene sitio en el torneo, no solo en el aula. Juega despacio y con la mano abierta."),
						N("É a partida que escreve o nome de {name} na ficha. Sem carta especial: só a vaga.", "This is the match that writes {name}'s name on the sheet. No special card: only the spot.", "Es la partida que escribe el nombre de {name} en la ficha. Sin carta especial: solo la plaza.")
					},
					new Line[] {
						N("Balamb Garden, partida de inscrição! Lina Faris, caloura, contra {name}!", "Balamb Garden, entry match! Lina Faris, freshman, against {name}!", "¡Balamb Garden, partida de inscripción! ¡Lina Faris, de primer año, contra {name}!"),
						N("Estilo aberto e defensivo, cartas à vista. A vaga no mundial começa nesta cadeira.", "Open, defensive style, cards face up. The road to the world tournament starts in this chair.", "Estilo abierto y defensivo, cartas a la vista. La plaza del mundial empieza en esta silla.")
					},
					say("npc_cadete", "Lina Faris", "Lina Faris", "Lina Faris",
						"Mão aberta, combinado? Você vê os meus ranks. Eu também vejo os seus. Se eu fechar um Same, meu nome entra na lista.",
						"Open hand, all right? You see my ranks. I see yours too. If I land a Same, my name goes on the list.",
						"¿Mano abierta, de acuerdo? Ves mis rangos. Yo también veo los tuyos. Si cierro un Same, mi nombre entra en la lista."),
					say("npc_cadete", "Lina Faris", "Lina Faris", "Lina Faris",
						"Você leu o lado que encostava. Eu deixei o 6 no canto errado. Pode entrar. Eu torço da plateia, sem vergonha.",
						"You read the side that was touching. I left the 6 on the wrong corner. You're in. I'll cheer from the stands, no shame.",
						"Leíste el lado que tocaba. Dejé el 6 en la esquina equivocada. Puedes entrar. Animo desde la grada, sin vergüenza."),
					lost("npc_cadete", "Lina Faris", "Lina Faris", "Lina Faris",
						"Same estava ali e você não olhou. Ainda dá. Encosta o mesmo rank, não o primeiro que vier à mão.",
						"Same was sitting there and you didn't look. Still time. Touch the same rank, not the first card in your hand.",
						"Same estaba ahí y no miraste. Todavía puedes. Encaja el mismo rango, no la primera carta de la mano."),
					new Line[] {
						N("Inscrição aceita! {name} está no chaveamento. A próxima cadeira é a do CC Spade.", "Entry accepted! {name} is on the ladder. Next chair: CC Spade.", "¡Inscripción aceptada! {name} está en la llave. La próxima silla es la de CC Spade."),
						Q("Open acabou. Daqui pra frente os ranks dela ficam fechados. Olha Same antes de soltar.", "Open is over. From here her ranks stay hidden. Look for Same before you let go.", "Open se acabó. A partir de aquí sus rangos quedan ocultos. Busca Same antes de soltar.")
					},
					new Line[] {
						N("A inscrição não fecha. A regra da seletiva é revanche na mesma chave. Lina continua sentada.", "Registration doesn't close. The qualifier rule is a rematch of the same tie. Lina stays seated.", "La inscripción no se cierra. La regla de la selectiva es revancha en la misma llave. Lina sigue sentada.")
					,
						Q("Open mostrou o 6. Você ainda pôs no lado errado. Olha o rank que encosta.",
							"Open showed the 6. You still put it on the wrong side. Watch the rank that touches.",
							"Open mostró el 6. Aun así lo pusiste en el lado equivocado. Mira el rango que toca.")
					}),
				duel("spade", "cc_spade", "CC Spade", "CC Spade", "CC Spade", 1, 3,
					Options.AIType.DEFENSIVE, Ruleset.BASIC, 0, false,
					"1ª chave", "1st tie", "1.ª llave",
					"Defensivo. Segura o canto e espera o erro.",
					"Defensive. He holds a corner and waits for the mistake.",
					"Defensivo. Aguanta la esquina y espera el error.",
					new Line[] {
						N("CC Spade cuida do caixa do grupo e empresta cartas a quem chega sem álbum.", "CC Spade keeps the club's box and lends cards to anyone who arrives without an album.", "CC Spade guarda la caja del grupo y presta cartas a quien llega sin álbum."),
						N("Entrou na seletiva para o CC Group existir fora dos corredores. Joga com paciência, quase sem falar.", "He entered the qualifier so the CC Group would exist outside the hall. He plays patiently, almost without talking.", "Entró en la selectiva para que el CC Group exista fuera de los pasillos. Juega con paciencia, casi sin hablar."),
						N("Esta é a primeira chave de verdade. Acabou a mão aberta. Sem carta especial.", "This is the first real tie. No more open hand. No special card.", "Esta es la primera llave de verdad. Se acabó la mano abierta. Sin carta especial.")
					},
					new Line[] {
						N("Primeira chave da seletiva! CC Spade, do CC Group, contra {name}!", "First tie of the qualifier! CC Spade, of the CC Group, against {name}!", "¡Primera llave de la selectiva! ¡CC Spade, del CC Group, contra {name}!"),
						N("Estilo defensivo. Ele não entrega o canto. Quem vencer enfrenta o Joker.", "Defensive style. He doesn't give up the corner. The winner faces Joker.", "Estilo defensivo. No entrega la esquina. Quien gane enfrenta a Joker.")
					},
					say("cc_spade", "CC Spade", "CC Spade", "CC Spade",
						"As cinco que eu emprestei acabam aqui. Sem Open. Eu seguro o canto e espero o seu Same falhar.",
						"The five I lent you end here. No Open. I'll hold the corner and wait for your Same to miss.",
						"Las cinco que te presté terminan aquí. Sin Open. Aguanto la esquina y espero que tu Same falle."),
					say("cc_spade", "CC Spade", "CC Spade", "CC Spade",
						"Você virou o canto que eu estava guardando. Fica com a vaga. O Joker não espera Combo incompleto.",
						"You flipped the corner I was holding. Keep the spot. Joker won't wait on a half-finished Combo.",
						"Volteaste la esquina que yo guardaba. Quédate la plaza. Joker no espera un Combo a medias."),
					lost("cc_spade", "CC Spade", "CC Spade", "CC Spade",
						"Cedo demais. Você soltou um 5 sem olhar o lado. Volta quando Same deixar de ser só um nome.",
						"Too soon. You dropped a 5 without checking the side. Come back when Same is more than a word.",
						"Demasiado pronto. Soltaste un 5 sin mirar el lado. Vuelve cuando Same sea más que un nombre."),
					new Line[] {
						N("{name} vence a primeira chave! O Joker espera no centro de treinamento.", "{name} clears the first tie! Joker is waiting at the training center.", "¡{name} supera la primera llave! Joker espera en el centro de entrenamiento."),
						Q("Ele segura o canto de propósito. O Joker pune se você jogar rápido demais. ...Whatever. Senta.", "He holds the corner on purpose. Joker punishes you if you rush. ...Whatever. Sit.", "Aguanta la esquina a propósito. Joker te castiga si te apresuras. ...Whatever. Siéntate.")
					},
					new Line[] {
						N("Derrota nesta chave não risca o nome. A revanche é contra o Spade, agora.", "A loss in this tie doesn't strike your name. The rematch is against Spade, right now.", "Una derrota en esta llave no tacha el nombre. La revancha es contra Spade, ahora.")
					,
						Q("Você soltou cedo demais. Same estava no canto. Ele esperou isso.",
							"You let go too soon. Same was on the corner. He was waiting for that.",
							"Soltaste demasiado pronto. Same estaba en la esquina. Él esperaba eso.")
					}),
				duel("joker", "cc_joker", "CC Joker", "CC Joker", "CC Joker", 2, 4,
					Options.AIType.DEFENSIVE, Ruleset.BASIC, 0, false,
					"2ª chave", "2nd tie", "2.ª llave",
					"Defensivo, com trocas curtas. Pune a pressa.",
					"Defensive, with short trades. He punishes a rush.",
					"Defensivo, con cambios cortos. Castiga la prisa.",
					new Line[] {
						N("CC Joker joga no centro de treinamento, longe do quadro de avisos do grupo.", "CC Joker plays at the training center, away from the club notice board.", "CC Joker juega en el centro de entrenamiento, lejos del tablón del grupo."),
						N("Entrou para mostrar que o CC Group não é só sala de aula. O humor fica fora da carta.", "He entered to show the CC Group isn't only a classroom. The humor stays off the card.", "Entró para mostrar que el CC Group no es solo un aula. El humor se queda fuera de la carta."),
						N("Segunda chave. Ainda sem prêmio especial. O Club vem depois, com Plus.", "Second tie. Still no special prize. Club comes next, with Plus.", "Segunda llave. Todavía sin premio especial. Club viene después, con Plus.")
					},
					new Line[] {
						N("Segunda chave! CC Joker contra {name}, no centro de treinamento do Garden!", "Second tie! CC Joker against {name}, at the Garden training center!", "¡Segunda llave! ¡CC Joker contra {name}, en el centro de entrenamiento del Garden!"),
						N("Ele troca pouco e espera. Quem vencer enfrenta o CC Club.", "He trades little and waits. The winner faces CC Club.", "Cambia poco y espera. Quien gane enfrenta a CC Club.")
					},
					say("cc_joker", "CC Joker", "CC Joker", "CC Joker",
						"Aqui eu troco carta, não piada. Same e Combo. Se você encostar dois iguais e não seguir a virada, eu fico com o tabuleiro.",
						"Out here I trade cards, not jokes. Same and Combo. If you touch two equals and don't follow the flip, I keep the board.",
						"Aquí cambio cartas, no chistes. Same y Combo. Si encajas dos iguales y no sigues el volteo, me quedo el tablero."),
					say("cc_joker", "CC Joker", "CC Joker", "CC Joker",
						"Você seguiu a virada até o fim. Tá valendo. O Club trata Plus como exercício. Não ri no meio da soma.",
						"You followed the flip all the way. It counts. Club treats Plus like a drill. Don't laugh in the middle of a sum.",
						"Seguiste el volteo hasta el final. Vale. Club trata Plus como un ejercicio. No te rías en medio de la suma."),
					lost("cc_joker", "CC Joker", "CC Joker", "CC Joker",
						"Sem pressa. Você abriu Combo e parou no segundo. Eu não saio desta cadeira.",
						"No rush. You started a Combo and stopped on the second card. I'm not leaving this chair.",
						"Sin prisa. Abriste un Combo y te paraste en la segunda. No me bajo de esta silla."),
					new Line[] {
						N("{name} passa pelo Joker! Próxima chave: CC Club, e Plus entra na mesa.", "{name} gets past Joker! Next tie: CC Club, and Plus comes into play.", "¡{name} supera a Joker! Próxima llave: CC Club, y Plus entra en la mesa."),
						Q("Ele calou quando o Combo fechou. Bom sinal. O Club já soma Plus na cabeça, antes de sentar.", "He went quiet when the Combo closed. Good sign. Club already adds Plus in his head, before he sits.", "Se calló cuando cerró el Combo. Buena señal. Club ya suma Plus en la cabeza, antes de sentarse.")
					},
					new Line[] {
						N("A segunda chave continua de pé. Revanche contra o Joker, mesma regra.", "The second tie is still standing. Rematch against Joker, same rule.", "La segunda llave sigue en pie. Revancha contra Joker, misma regla.")
					,
						Q("Combo parado no segundo. Ele não sai da cadeira por isso.",
							"Combo stopped on the second card. That's why he isn't leaving the chair.",
							"Combo parado en la segunda. Por eso no se baja de la silla.")
					}),
				duel("club", "cc_club", "CC Club", "CC Club", "CC Club", 3, 4,
					Options.AIType.BALANCED, Ruleset.PLUS, 0, false,
					"Quartas da seletiva", "Qualifier quarterfinal", "Cuartos de la selectiva",
					"Plus e Combo. Joga como num exercício de campo.",
					"Plus and Combo. He plays it like a field drill.",
					"Plus y Combo. Lo juega como un ejercicio de campo.",
					new Line[] {
						N("CC Club é candidato a SeeD. Para ele, a seletiva é só mais um treino com plateia.", "CC Club is a SeeD candidate. For him the qualifier is one more drill with an audience.", "CC Club es candidato a SeeD. Para él, la selectiva es un entrenamiento más con público."),
						N("Quer um histórico limpo antes do exame de campo. Não empresta carta e não conta história.", "He wants a clean record before the field exam. He doesn't lend cards or tell stories.", "Quiere un historial limpio antes del examen de campo. No presta cartas ni cuenta historias."),
						N("Quartas da seletiva. Plus e Combo. A Xu espera na semifinal.", "Qualifier quarterfinal. Plus and Combo. Xu is waiting in the semifinal.", "Cuartos de la selectiva. Plus y Combo. Xu espera en la semifinal.")
					},
					new Line[] {
						N("Quartas da seletiva do Garden! CC Club contra {name}!", "Garden qualifier quarterfinal! CC Club against {name}!", "¡Cuartos de la selectiva del Garden! ¡CC Club contra {name}!"),
						N("Plus está ligado. Ele soma os lados como quem confere um mapa. Em jogo, a vaga na semifinal.", "Plus is on. He adds up the sides like someone checking a map. At stake: a semifinal spot.", "Plus está activo. Suma los lados como quien revisa un mapa. En juego, la plaza en la semifinal.")
					},
					say("cc_club", "CC Club", "CC Club", "CC Club",
						"Candidato a SeeD também se senta. Hoje Same e Plus andam juntos. Se a soma der o mesmo número dos dois lados, eu viro os dois. Sem conversa no meio.",
						"A SeeD candidate sits too. Today Same and Plus travel together. If both sides add to the same number, I flip both. No talk in the middle.",
						"Un candidato a SeeD también se sienta. Hoy Same y Plus van juntos. Si la suma da el mismo número a ambos lados, volteo los dos. Sin charla en medio."),
					say("cc_club", "CC Club", "CC Club", "CC Club",
						"Você fechou o Plus que eu deixei aberto. Registrado. A Xu não facilita nem para colega de grupo. Ela soma primeiro, fala depois.",
						"You closed the Plus I left open. Logged. Xu doesn't go easy even on a clubmate. She adds first, talks later.",
						"Cerraste el Plus que dejé abierto. Registrado. Xu no facilita ni a un compañero del grupo. Primero suma, después habla."),
					lost("cc_club", "CC Club", "CC Club", "CC Club",
						"Plus pune a pressa. Você somou 10 de um lado e 8 do outro e soltou assim mesmo. De novo: some antes de soltar.",
						"Plus punishes a rush. You added 10 on one side and 8 on the other and still let go. Again: add it up before you drop it.",
						"Plus castiga la prisa. Sumaste 10 de un lado y 8 del otro y aun así soltaste. Otra vez: suma antes de soltar."),
					new Line[] {
						N("Quartas superadas! {name} enfrenta a Xu na semifinal da seletiva.", "Quarterfinal cleared! {name} faces Xu in the qualifier semifinal.", "¡Cuartos superados! {name} se enfrenta a Xu en la semifinal de la selectiva."),
						Q("O exercício dele é limpo. A Xu não ensina: ela fecha a soma e cala. Não peça dica no meio da mão.", "His drill is clean. Xu doesn't teach: she finishes the sum and goes quiet. Don't ask for a hint mid-hand.", "Su ejercicio es limpio. Xu no enseña: cierra la suma y se calla. No pidas pista a mitad de la mano.")
					},
					new Line[] {
						N("As quartas não eliminam ninguém. A revanche é nesta mesa, com Plus ligado.", "The quarterfinal doesn't knock you out. The rematch is at this table, with Plus on.", "Los cuartos no eliminan a nadie. La revancha es en esta mesa, con Plus activo.")
					,
						Q("A soma não fechou. Plus não perdoa um 10 contra um 8.",
							"The sum didn't close. Plus doesn't forgive a 10 against an 8.",
							"La suma no cerró. Plus no perdona un 10 contra un 8.")
					}),
				duel("heart", "cc_heart", "Xu", "Xu", "Xu", 3, 5,
					Options.AIType.BALANCED, Ruleset.PLUS, 0, false,
					"Semifinal da seletiva", "Qualifier semifinal", "Semifinal de la selectiva",
					"Analítica. Plus primeiro, nenhuma conversa.",
					"Analytical. Plus first, no conversation.",
					"Analítica. Plus primero, nada de charla.",
					new Line[] {
						N("Xu, a Heart do CC Group. Assistente de instrução. Não é a Quistis e não fala como ela.", "Xu, the CC Group's Heart. A teaching assistant. She is not Quistis and doesn't talk like her.", "Xu, la Heart del CC Group. Ayudante de instrucción. No es Quistis y no habla como ella."),
						N("Entrou para ver se as anotações dela sobre Plus sobrevivem a uma mesa ao vivo.", "She entered to see whether her notes on Plus survive a live table.", "Entró para ver si sus apuntes sobre Plus sobreviven a una mesa en vivo."),
						N("Semifinal. Quem passar senta na final contra a Quistis, e o Ifrit está naquela mesa.", "Semifinal. Whoever wins sits in the final against Quistis, and Ifrit is on that table.", "Semifinal. Quien pase se sienta en la final contra Quistis, e Ifrit está en esa mesa.")
					},
					new Line[] {
						N("Semifinal da seletiva! Xu, do CC Group, contra {name}!", "Qualifier semifinal! Xu, of the CC Group, against {name}!", "¡Semifinal de la selectiva! ¡Xu, del CC Group, contra {name}!"),
						N("Ela joga Plus sem enfeite. Em jogo, a vaga na final do Garden.", "She plays Plus without frills. At stake: a spot in the Garden final.", "Juega Plus sin adornos. En juego, la plaza en la final del Garden.")
					},
					say("cc_heart", "Xu", "Xu", "Xu",
						"Xu. Plus primeiro. Eu não entrego uma casa sorrindo. A Quistis que espere o resultado desta soma.",
						"Xu. Plus first. I don't give up a square with a smile. Quistis can wait on this sum.",
						"Xu. Plus primero. No entrego una casilla sonriendo. Que Quistis espere el resultado de esta suma."),
					say("cc_heart", "Xu", "Xu", "Xu",
						"Você leu o Plus no terceiro lance. Passou. Não me peça conselho. A Quistis explica depois, se ainda houver o que explicar.",
						"You caught the Plus on the third play. You pass. Don't ask me for advice. Quistis will explain later, if there's anything left to explain.",
						"Leíste el Plus en la tercera jugada. Pasaste. No me pidas consejo. Quistis explica después, si aún queda algo que explicar."),
					lost("cc_heart", "Xu", "Xu", "Xu",
						"Soma de novo. Você deixou 4 e 6 encostados e não viu o 10. Eu só anoto o mesmo erro uma vez.",
						"Add it up again. You left a 4 and a 6 touching and missed the 10. I only log the same mistake once.",
						"Suma otra vez. Dejaste un 4 y un 6 juntos y no viste el 10. Solo anoto el mismo error una vez."),
					new Line[] {
						N("{name} na final da seletiva! A adversária é a Quistis. O Ifrit só sai com a vitória.", "{name} is in the qualifier final! The opponent is Quistis. Ifrit only leaves with the winner.", "¡{name} está en la final de la selectiva! La rival es Quistis. Ifrit solo sale con la victoria."),
						Q("Ela respeita a soma, não o discurso. Lembre-se disso quando a Quistis sentar. Ifrit não sai numa virada pela metade.", "She respects the sum, not a speech. Remember that when Quistis sits. Ifrit doesn't leave on a half-finished flip.", "Ella respeta la suma, no el discurso. Recuérdalo cuando se siente Quistis. Ifrit no se va con un volteo a medias.")
					},
					new Line[] {
						N("A semifinal não termina com derrota. Revanche contra a Xu. A final continua esperando.", "The semifinal doesn't end on a loss. Rematch against Xu. The final is still waiting.", "La semifinal no termina con una derrota. Revancha contra Xu. La final sigue esperando.")
					,
						Q("4 e 6 encostados. Era 10. Ela só anota uma vez.",
							"4 and 6 touching. That was 10. She only logs it once.",
							"4 y 6 juntos. Era 10. Ella solo lo anota una vez.")
					}),
				duel("king", "cc_king", "Quistis", "Quistis", "Quistis", 4, 5,
					Options.AIType.BALANCED, Ruleset.PLUS, 85, false,
					"Final da seletiva", "Qualifier final", "Final de la selectiva",
					"Experiente. Plus e Combo, leitura longa.",
					"Experienced. Plus and Combo, a long read.",
					"Experta. Plus y Combo, lectura larga.",
					new Line[] {
						N("Quistis Trepe, instrutora e King do CC Group. Não é a Xu: ela explica, a Xu só mede.", "Quistis Trepe, instructor and King of the CC Group. She isn't Xu: she explains, Xu only measures.", "Quistis Trepe, instructora y King del CC Group. No es Xu: ella explica, Xu solo mide."),
						N("Entrou para que a última palavra do Garden seja de quem aguenta explicar uma derrota.", "She entered so the Garden's last word would belong to someone who can explain a loss.", "Entró para que la última palabra del Garden sea de quien sabe explicar una derrota."),
						N("O Ifrit é o prêmio fixo desta final. Não está na mão comum. Só sai na primeira vitória.", "Ifrit is this final's fixed prize. It isn't in the regular hand. It only comes with the first win.", "Ifrit es el premio fijo de esta final. No está en la mano común. Solo sale en la primera victoria.")
					},
					reveal(new Line[] {
						N("Final da seletiva de Balamb Garden! Quistis contra {name}!", "Balamb Garden qualifier final! Quistis against {name}!", "¡Final de la selectiva de Balamb Garden! ¡Quistis contra {name}!"),
						N("Plus e Combo. Ela conduz a mesa. Em jogo: a vaga no Torneio Mundial e, na primeira vitória, o Ifrit.", "Plus and Combo. She runs the table. At stake: a spot in the World Tournament and, on the first win, Ifrit.", "Plus y Combo. Ella dirige la mesa. En juego: la plaza en el Torneo Mundial y, en la primera victoria, Ifrit.")
					}),
					reveal(say("cc_king", "Quistis", "Quistis", "Quistis",
						"{name}. Plus e Combo, leitura longa. Se a partida for sua, o Ifrit vai junto. Eu continuo dando aula à tarde.",
						"{name}. Plus and Combo, a long read. If the match is yours, Ifrit goes with it. I still teach in the afternoon.",
						"{name}. Plus y Combo, lectura larga. Si la partida es tuya, Ifrit se va contigo. Yo sigo dando clase por la tarde.")),
					say("cc_king", "Quistis", "Quistis", "Quistis",
						"Você fechou o Combo no último lance. O Ifrit é seu. No porto a regra muda: Plus ainda vale, e a tabela passa a contar pontos. Não confunda isso com o fim.",
						"You closed the Combo on the last play. Ifrit is yours. At the harbor the rule shifts: Plus still counts, and the table starts scoring. Don't mistake this for the end.",
						"Cerraste el Combo en la última jugada. Ifrit es tuyo. En el puerto la regla cambia: Plus sigue valiendo, y la tabla empieza a contar puntos. No lo confundas con el final."),
					lost("cc_king", "Quistis", "Quistis", "Quistis",
						"Ainda não. Você abriu Plus e não seguiu o Combo. Respira, soma e senta de novo. A final desta chave não muda de nome.",
						"Not yet. You opened Plus and didn't follow the Combo. Breathe, add it up and sit down again. This ladder final doesn't change its name.",
						"Todavía no. Abriste Plus y no seguiste el Combo. Respira, suma y siéntate otra vez. La final de esta llave no cambia de nombre."),
					new Line[] {
						N("{name} vence a seletiva e entra no mundial! Próxima etapa: classificatória no porto de Balamb.", "{name} wins the qualifier and enters the world tournament! Next stage: the qualifying group at Balamb harbor.", "¡{name} gana la selectiva y entra al mundial! Próxima etapa: clasificatoria en el puerto de Balamb."),
						Q("Você segurou o Combo até o fim. No porto a tabela vale pontos: vitória vale 3. Eu vou junto. Não desperdice uma soma.", "You held the Combo to the end. At the harbor the table scores: a win is worth 3. I'm coming along. Don't waste a sum.", "Aguantaste el Combo hasta el final. En el puerto la tabla puntúa: una victoria vale 3. Voy contigo. No desperdicies una suma.")
					},
					new Line[] {
						N("A final da seletiva tem revanche na hora. A vaga no mundial só sai com a sua vitória.", "The qualifier final has an instant rematch. The world-tournament spot only comes with your win.", "La final de la selectiva tiene revancha al momento. La plaza del mundial solo llega con tu victoria.")
					,
						Q("Você abriu Plus e parou o Combo. A Quistis não para.",
							"You opened Plus and stopped the Combo. Quistis doesn't stop.",
							"Abriste Plus y paraste el Combo. Quistis no se detiene.")
					})
			});
	}

	private static Region town() {
		return region("town", "chegada_balamb_town", "mesa_balamb_town.png", Kind.GROUP, 2,
			new String[][] {{"queen_town", "raijin"}, {"queen_town", "fujin"}, {"raijin", "fujin"}},
			"Balamb - Classificatória", "Balamb - Qualifying", "Balamb - Clasificatoria",
			new Line[] {
				Q("Você ganhou o Ifrit no Combo da Quistis. Aqui Plus continua, e cada mesa vale 3 pontos. Não trate isto como aquecimento.", "You won Ifrit on Quistis's Combo. Plus is still on here, and each table is worth 3 points. Don't treat this as a warm-up.", "Ganaste a Ifrit en el Combo de Quistis. Aquí Plus sigue, y cada mesa vale 3 puntos. No lo tomes como un calentamiento."),
				N("O porto sedia o Grupo do Porto porque o salão do hotel comporta quatro mesas e o cais traz o público.", "The harbor hosts the Harbor Group because the hotel hall fits four tables and the docks bring the crowd.", "El puerto acoge el Grupo del Puerto porque en el salón del hotel caben cuatro mesas y el muelle trae al público."),
				N("Quatro jogadores, todos contra todos. Vitória vale 3. Os outros jogos já estão na tabela. Classificam-se os dois primeiros.", "Four players, round robin. A win is worth 3. The other matches are already on the board. The top two qualify.", "Cuatro jugadores, todos contra todos. La victoria vale 3. Los otros partidos ya están en la tabla. Se clasifican los dos primeros."),
				Q("{name}, os dois primeiros avançam. A Rainha recusou a folga e sentou neste grupo. Ela não abre a mão. Cuida dos cantos.", "{name}, the top two advance. The Queen refused the bye and sat in this group. She doesn't open her hand. Watch the corners.", "{name}, avanzan los dos primeros. La Reina rechazó el descanso y se sentó en este grupo. No abre la mano. Cuida las esquinas."),
				say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
					"Eu defendo o título em público, não no papel. Se eu passar em segundo, a gente se vê mais adiante.",
					"I defend the title in public, not on paper. If I go through in second, we'll meet further on.",
					"Defiendo el título en público, no en el papel. Si paso segunda, nos vemos más adelante.")
			},
			new Duel[] {
				duel("queen_town", "card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas", 4, 5,
					Options.AIType.BALANCED, Ruleset.PLUS, 84, true,
					"Classificatória", "Qualifying match", "Clasificatoria",
					"Controle de canto e Plus. Não abre a mão.",
					"Corner control and Plus. She doesn't open her hand.",
					"Control de esquina y Plus. No abre la mano.",
					new Line[] {
						N("A Rainha das Cartas percorre o circuito. Recusou a folga de cabeça de chave para jogar o Grupo do Porto.", "The Queen of Cards travels the circuit. She turned down a seeded bye to play in the Harbor Group.", "La Reina de las Cartas recorre el circuito. Rechazó la exención de cabeza de serie para jugar el Grupo del Puerto."),
						N("Quer defender o título diante de quem chegou de barco. Trata {name} como estreante, ainda sem ironia.", "She wants to defend the title in front of the people who came by boat. She still treats {name} as a newcomer, without irony.", "Quiere defender el título delante de quien llegó en barco. Trata a {name} como debutante, todavía sin ironía."),
						N("A Shiva é o prêmio fixo deste jogo. Não entra na mão. Na primeira vitória, ela muda de dono, além da carta escolhida.", "Shiva is this match's fixed prize. She isn't in the hand. On the first win she changes owners, on top of the chosen card.", "Shiva es el premio fijo de este partido. No entra en la mano. En la primera victoria cambia de dueño, además de la carta elegida.")
					},
					new Line[] {
						N("Balamb Town, Grupo do Porto! A Rainha das Cartas contra {name}!", "Balamb Town, Harbor Group! The Queen of Cards against {name}!", "¡Balamb Town, Grupo del Puerto! ¡La Reina de las Cartas contra {name}!"),
						N("Plus. Ela segura o canto. Três pontos neste jogo. Os dois primeiros do grupo seguem para a fase de Timber.", "Plus. She holds the corner. Three points in this match. The group's top two go on to the Timber phase.", "Plus. Ella aguanta la esquina. Tres puntos en este partido. Los dos primeros del grupo pasan a la fase de Timber.")
					},
					say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"A Shiva está comigo faz tempo. Plus, mão fechada. Se você virar o canto que eu escolher, ela viaja. Os 3 pontos, eu ainda quero.",
						"Shiva has been with me a long time. Plus, closed hand. If you flip the corner I pick, she travels. I still want the 3 points.",
						"Shiva está conmigo desde hace tiempo. Plus, mano cerrada. Si volteas la esquina que yo elija, ella viaja. Los 3 puntos, aún los quiero."),
					say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"Você me tirou o canto com Plus. Leva a Shiva. Três pontos seus. Raijin empurra o centro. Fujin não avisa o Plus.",
						"You took my corner with Plus. Take Shiva. Three points to you. Raijin pushes the center. Fujin doesn't warn you about Plus.",
						"Me quitaste la esquina con Plus. Llévate a Shiva. Tres puntos tuyos. Raijin empuja el centro. Fujin no avisa el Plus."),
					lost("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"Quase. Você deixou o canto com 6 e eu completei o Plus. A Shiva continua na bolsa, e os pontos não entram.",
						"Almost. You left the corner on a 6 and I finished the Plus. Shiva stays in the bag, and the points stay off the table.",
						"Casi. Dejaste la esquina en un 6 y yo cerré el Plus. Shiva sigue en la bolsa, y los puntos no entran."),
					new Line[] {
						N("Três pontos para {name}! A Rainha segue no grupo: derrota não elimina nesta fase. Raijin é o próximo.", "Three points for {name}! The Queen stays in the group: a loss doesn't eliminate anyone in this phase. Raijin is next.", "¡Tres puntos para {name}! La Reina sigue en el grupo: la derrota no elimina en esta fase. Raijin es el siguiente."),
						Q("Ela perdeu o canto, não o torneio. Raijin grita e joga no meio. Não acompanhe o volume. Acompanhe o rank.", "She lost the corner, not the tournament. Raijin shouts and plays the center. Don't follow the volume. Follow the rank.", "Perdió la esquina, no el torneo. Raijin grita y juega al centro. No sigas el volumen. Sigue el rango.")
					},
					new Line[] {
						N("Zero pontos, e a derrota não entra na tabela. A revanche substitui o resultado quando você vencer.", "Zero points, and the loss stays off the table. The rematch replaces the result when you win.", "Cero puntos, y la derrota no entra en la tabla. La revancha sustituye el resultado cuando ganes.")
					,
						Q("O canto ficou no 6. Ela completou o Plus. Shiva não sai assim.",
							"The corner sat on a 6. She finished the Plus. Shiva doesn't leave like that.",
							"La esquina quedó en un 6. Ella cerró el Plus. Shiva no se va así.")
					}),
				duel("raijin", "raijin", "Raijin", "Raijin", "Raijin", 4, 6,
					Options.AIType.BALANCED, Ruleset.PLUS, 79, true,
					"Classificatória", "Qualifying match", "Clasificatoria",
					"Jogo barulhento e Plus. Ele empurra o centro.",
					"Loud game and Plus. He pushes the center.",
					"Juego ruidoso y Plus. Empuja el centro.",
					new Line[] {
						N("Raijin veio com a Fujin no mesmo barco. Entrou porque o salão do hotel estava barulhento, e ele gosta de salão barulhento.", "Raijin came on the same boat as Fujin. He entered because the hotel hall was loud, and he likes a loud hall.", "Raijin vino en el mismo barco que Fujin. Entró porque el salón del hotel estaba ruidoso, y a él le gustan los salones ruidosos."),
						N("Quer os três pontos e quer que a Fujin veja. Não joga mal: joga forte e fala o tempo todo.", "He wants the three points and he wants Fujin to see it. He's no fool at the table: he plays hard and talks the whole time.", "Quiere los tres puntos y quiere que Fujin lo vea. No juega mal: juega fuerte y habla todo el tiempo."),
						N("O Angelo é o prêmio fixo. Não está entre as cinco da mão.", "Angelo is the fixed prize. It isn't among the five in the hand.", "Angelo es el premio fijo. No está entre las cinco de la mano.")
					},
					new Line[] {
						N("Segue o Grupo do Porto! Raijin contra {name}!", "The Harbor Group continues! Raijin against {name}!", "¡Sigue el Grupo del Puerto! ¡Raijin contra {name}!"),
						N("Ele joga no centro com Plus. Três pontos em jogo. A Fujin já perdeu para ele na outra mesa.", "He plays the center with Plus. Three points at stake. Fujin already lost to him at the other table.", "Juega el centro con Plus. Tres puntos en juego. Fujin ya perdió contra él en la otra mesa.")
					},
					say("raijin", "Raijin", "Raijin", "Raijin",
						"Ya, bora! Plus no meio do tabuleiro. O Angelo tá aqui e eu não empresto. A Fujin disse pra eu não entregar o centro, ya know.",
						"Ya, let's go! Plus in the middle of the board. Angelo's here and I'm not lending him out. Fujin told me not to give up the center, ya know.",
						"¡Ya, vamos! Plus en el centro del tablero. Angelo está aquí y no lo presto. Fujin me dijo que no entregue el centro, ya know."),
					say("raijin", "Raijin", "Raijin", "Raijin",
						"Ya... você virou o centro que eu empurrei. Fica com o Angelo. A Fujin vai resmungar. Três pontos seus, ya know.",
						"Ya... you flipped the center I was pushing. Keep Angelo. Fujin's gonna grumble. Three points to you, ya know.",
						"Ya... volteaste el centro que yo empujaba. Quédate a Angelo. Fujin va a rezongar. Tres puntos tuyos, ya know."),
					lost("raijin", "Raijin", "Raijin", "Raijin",
						"Ya, essa foi minha! Você deixou o 8 no meio e eu fechei o Plus dos dois lados. Outra? Eu ainda tô sentado, ya know.",
						"Ya, that one was mine! You left the 8 in the middle and I closed Plus on both sides. Another? I'm still sitting, ya know.",
						"¡Ya, esa fue mía! Dejaste el 8 en el medio y cerré Plus a ambos lados. ¿Otra? Sigo sentado, ya know."),
					new Line[] {
						N("{name} soma três no Grupo do Porto! Falta a Fujin para fechar a sua tabela.", "{name} adds three in the Harbor Group! Only Fujin is left on your card.", "¡{name} suma tres en el Grupo del Puerto! Falta Fujin para cerrar tu tabla."),
						Q("Ele grita, mas o 8 no centro era sério. A Fujin não avisa. Plus curto, ofensivo. Não relaxe.", "He shouts, but that 8 in the center was serious. Fujin doesn't warn you. Short, offensive Plus. Don't ease up.", "Grita, pero el 8 del centro iba en serio. Fujin no avisa. Plus corto, ofensivo. No te relajes.")
					},
					new Line[] {
						N("O ponto do Raijin não conta contra você. Revanche na mesma mesa. A tabela espera a vitória.", "Raijin's points don't count against you. Rematch at the same table. The standings are waiting for the win.", "Los puntos de Raijin no cuentan en tu contra. Revancha en la misma mesa. La tabla espera la victoria.")
					,
						Q("O 8 no meio era isca. Ele fechou Plus dos dois lados.",
							"The 8 in the middle was bait. He closed Plus on both sides.",
							"El 8 del medio era cebo. Cerró Plus a ambos lados.")
					}),
				duel("fujin", "fujin", "Fujin", "Fujin", "Fujin", 5, 6,
					Options.AIType.OFFENSIVE, Ruleset.PLUS, 83, true,
					"Classificatória", "Qualifying match", "Clasificatoria",
					"Ofensiva e curta. Plus sem aviso.",
					"Offensive and short. Plus without warning.",
					"Ofensiva y corta. Plus sin aviso.",
					new Line[] {
						N("Fujin entrou no mesmo grupo para o Raijin não entregar a mesa, e porque quer um histórico limpo só dela.", "Fujin entered the same group so Raijin wouldn't give the table away, and because she wants a clean record of her own.", "Fujin entró en el mismo grupo para que Raijin no regalara la mesa, y porque quiere un historial limpio solo suyo."),
						N("Fala pouco. Joga para a frente. Não repete o discurso de ninguém do Garden.", "She says little. She plays forward. She doesn't repeat anyone's speech from the Garden.", "Habla poco. Juega hacia adelante. No repite el discurso de nadie del Garden."),
						N("O Quezacotl é o prêmio fixo desta partida. Fecha o Grupo do Porto se {name} vencer.", "Quezacotl is this match's fixed prize. It closes the Harbor Group if {name} wins.", "Quezacotl es el premio fijo de esta partida. Cierra el Grupo del Puerto si {name} gana.")
					},
					new Line[] {
						N("Último jogo de {name} no Grupo do Porto! Fujin!", "{name}'s last match in the Harbor Group! Fujin!", "¡Último partido de {name} en el Grupo del Puerto! ¡Fujin!"),
						N("Ofensiva, Plus. Três pontos garantem a vaga com o que já está na tabela. Os dois primeiros avançam.", "Offensive, Plus. Three points seal the spot with what's already on the board. The top two advance.", "Ofensiva, Plus. Tres puntos aseguran la plaza con lo que ya está en la tabla. Los dos primeros avanzan.")
					},
					say("fujin", "Fujin", "Fujin", "Fujin",
						"RIVAL. QUEZACOTL NA MESA. PLUS SEM AVISO. RAIJIN: FALOU DEMAIS.",
						"RIVAL. QUEZACOTL ON THE TABLE. PLUS, NO WARNING. RAIJIN: TALKED TOO MUCH.",
						"RIVAL. QUEZACOTL EN LA MESA. PLUS SIN AVISO. RAIJIN: HABLÓ DE MÁS."),
					say("fujin", "Fujin", "Fujin", "Fujin",
						"FORTE. VOCÊ FECHOU O PLUS QUE EU ABRI. QUEZACOTL É SEU. SEM BRAVATA NO CAIS.",
						"STRONG. YOU CLOSED THE PLUS I OPENED. QUEZACOTL IS YOURS. NO BOASTING ON THE DOCK.",
						"FUERTE. CERRASTE EL PLUS QUE YO ABRÍ. QUEZACOTL ES TUYO. NADA DE ALARDES EN EL MUELLE."),
					lost("fujin", "Fujin", "Fujin", "Fujin",
						"FRACO. VOCÊ NÃO VIU O PLUS. DE NOVO.",
						"WEAK. YOU MISSED THE PLUS. AGAIN.",
						"DÉBIL. NO VISTE EL PLUS. OTRA VEZ."),
					new Line[] {
						N("Grupo do Porto encerrado para {name}! Confira a tabela: o corte é o segundo lugar. A Rainha segue no torneio pelo outro resultado.", "The Harbor Group is closed for {name}! Check the standings: the cutoff is second place. The Queen stays in the tournament on her other results.", "¡Grupo del Puerto cerrado para {name}! Revisa la tabla: el corte es el segundo lugar. La Reina sigue en el torneo por sus otros resultados."),
						Q("Ela não avisa o Plus. Você avisou o dela. Em Timber o tabuleiro ganha elemento. Leia a casa antes da carta.", "She doesn't warn you about Plus. You called hers. In Timber the board gets an element. Read the square before the card.", "Ella no avisa el Plus. Tú leíste el suyo. En Timber el tablero gana elemento. Lee la casilla antes que la carta.")
					},
					new Line[] {
						N("Sem os três pontos da Fujin, a conta muda. Revanche agora. Só a vitória entra na tabela.", "Without Fujin's three points, the math changes. Rematch now. Only the win goes on the board.", "Sin los tres puntos de Fujin, la cuenta cambia. Revancha ahora. Solo la victoria entra en la tabla.")
					,
						Q("Ela não avisa o Plus. Você também não viu.",
							"She doesn't warn you about Plus. You didn't see it either.",
							"Ella no avisa el Plus. Tú tampoco lo viste.")
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
				Q("No porto você passou do corte. Aqui o tabuleiro tem elemento. Uma carta certa na casa errada vira contra você.", "You made the cutoff at the harbor. Here the board has an element. The right card on the wrong square turns against you.", "En el puerto pasaste el corte. Aquí el tablero tiene elemento. La carta correcta en la casilla equivocada se vuelve en tu contra."),
				N("Timber sedia os grupos porque a estação une as linhas. O salão fica em cima dos trilhos, não num campo de batalha.", "Timber hosts the groups because the station links the lines. The hall sits above the tracks, not on a battlefield.", "Timber acoge los grupos porque la estación une las líneas. El salón queda encima de las vías, no en un campo de batalla."),
				N("Grupo dos Trilhos, cinco jogadores! Vitória vale 3. Os três primeiros vão para a fase principal.", "Rail Group, five players! A win is worth 3. The top three go on to the main stage.", "¡Grupo de los Raíles, cinco jugadores! La victoria vale 3. Los tres primeros pasan a la fase principal."),
				Q("{name}, a Rainha e o Seifer caíram em outro grupo. Do seu lado: Zone, Jack, Holt e Rinoa. Jack lê o elemento antes da carta. Faça o mesmo.", "{name}, the Queen and Seifer landed in another group. Your side: Zone, Jack, Holt and Rinoa. Jack reads the element before the card. Do the same.", "{name}, la Reina y Seifer cayeron en otro grupo. A tu lado: Zone, Jack, Holt y Rinoa. Jack lee el elemento antes que la carta. Haz lo mismo."),
				say("rinoa", "Rinoa", "Rinoa", "Rinoa",
					"Os Forest Owls daqui são o círculo de cartas da estação. Se vier jogar limpo, a gente se respeita.",
					"The Forest Owls here are the station's card circle. Play it straight and we'll respect each other.",
					"Los Forest Owls de aquí son el círculo de cartas de la estación. Juega limpio y nos respetaremos.")
			},
			new Duel[] {
				duel("zone", "zone_watts", "Zone e Watts", "Zone and Watts", "Zone y Watts", 4, 6,
					Options.AIType.BALANCED, Ruleset.PLUS, 82, true,
					"Grupo dos Trilhos", "Rail Group", "Grupo de los Raíles",
					"Plus espalhafatoso. Zone joga, Watts filma.",
					"Flashy Plus. Zone plays, Watts films.",
					"Plus ruidoso. Zone juega, Watts filma.",
					new Line[] {
						N("Zone e Watts filmam o torneio. Entraram no Grupo dos Trilhos com um convite local da estação.", "Zone and Watts are filming the tournament. They entered the Rail Group on a local invite from the station.", "Zone y Watts filman el torneo. Entraron en el Grupo de los Raíles con una invitación local de la estación."),
						N("Querem o documentário e, já que se sentaram, querem os três pontos. Zone fala. Watts não larga a câmera.", "They want the documentary and, since they sat down, they want the three points. Zone talks. Watts doesn't let go of the camera.", "Quieren el documental y, ya que se sentaron, quieren los tres puntos. Zone habla. Watts no suelta la cámara."),
						N("O Chicobo é o prêmio fixo. Não está na mão que eles jogam.", "Chicobo is the fixed prize. It isn't in the hand they play.", "Chicobo es el premio fijo. No está en la mano que juegan.")
					},
					new Line[] {
						N("Timber, Grupo dos Trilhos! Zone e Watts contra {name}! Uma inscrição só.", "Timber, Rail Group! Zone and Watts against {name}! A single entry.", "¡Timber, Grupo de los Raíles! ¡Zone y Watts contra {name}! Una sola inscripción."),
						N("Plus, jogo para a câmera. Três pontos. Os outros resultados do grupo já estão no quadro.", "Plus, played for the camera. Three points. The group's other results are already on the board.", "Plus, juego para la cámara. Tres puntos. Los otros resultados del grupo ya están en el cuadro.")
					},
					say("zone_watts", "Zone", "Zone", "Zone",
						"Watts, não corta! {name}, Plus espalhafatoso. Se você virar três de uma vez, o Chicobo entra nos créditos. Se eu virar, também.",
						"Watts, don't cut! {name}, flashy Plus. If you flip three at once, Chicobo goes in the credits. If I flip them, so does he.",
						"¡Watts, no cortes! {name}, Plus ruidoso. Si volteas tres de una vez, Chicobo entra en los créditos. Si los volteo yo, también."),
					say("zone_watts", "Zone", "Zone", "Zone",
						"Corta! Você fechou o Plus na frente da câmera. O Chicobo é seu. A gente continua filmando. O Jack, no próximo, não posa.",
						"Cut! You closed Plus in front of the camera. Chicobo is yours. We keep filming. Jack, next up, doesn't pose.",
						"¡Corten! Cerraste el Plus frente a la cámara. Chicobo es tuyo. Seguimos filmando. Jack, el siguiente, no posa."),
					lost("zone_watts", "Zone", "Zone", "Zone",
						"De novo, do começo. Você atropelou o Plus para sair na fita. Watts, continua gravando. Eu fico com esta mesa.",
						"Again, from the top. You rushed Plus to look good on tape. Watts, keep rolling. I'm keeping this table.",
						"Otra vez, desde el inicio. Atropellaste el Plus para salir en la cinta. Watts, sigue grabando. Me quedo con esta mesa."),
					new Line[] {
						N("Três pontos no Grupo dos Trilhos! O próximo é o CC Jack, que veio de trem para ler os elementos.", "Three points in the Rail Group! Next up is CC Jack, who came by train to read the elements.", "¡Tres puntos en el Grupo de los Raíles! El siguiente es CC Jack, que vino en tren a leer los elementos."),
						Q("Não jogue para a câmera. O Jack lê o elemento da casa antes de escolher a carta. Copie isso, não o volume.", "Don't play to the camera. Jack reads the square's element before he picks a card. Copy that, not the volume.", "No juegues para la cámara. Jack lee el elemento de la casilla antes de elegir la carta. Copia eso, no el volumen.")
					},
					new Line[] {
						N("A cena não vale ponto. Revanche contra Zone e Watts. A tabela só muda com a vitória.", "The scene isn't worth points. Rematch against Zone and Watts. The standings only change with a win.", "La escena no vale puntos. Revancha contra Zone y Watts. La tabla solo cambia con la victoria.")
					,
						Q("Você jogou para a câmera. O Plus não espera pose.",
							"You played to the camera. Plus doesn't wait for a pose.",
							"Jugaste para la cámara. Plus no espera una pose.")
					}),
				duel("jack", "cc_jack", "CC Jack", "CC Jack", "CC Jack", 5, 6,
					Options.AIType.BALANCED, Ruleset.ELEMENT, 80, true,
					"Grupo dos Trilhos", "Rail Group", "Grupo de los Raíles",
					"Lê o elemento do tabuleiro antes da carta.",
					"He reads the board's element before the card.",
					"Lee el elemento del tablero antes de la carta.",
					new Line[] {
						N("CC Jack é o leitor de elementos do CC Group. Veio de trem, não com o Spade nem com a Xu.", "CC Jack is the CC Group's element reader. He came by train, not with Spade or Xu.", "CC Jack es el lector de elementos del CC Group. Vino en tren, no con Spade ni con Xu."),
						N("Quer provar que a regra Elemental cabe numa fase de grupos, não só numa sala do Garden.", "He wants to prove the Elemental rule belongs in a group stage, not just in a Garden classroom.", "Quiere demostrar que la regla Elemental cabe en una fase de grupos, no solo en un aula del Garden."),
						N("O Gilgamesh é o prêmio fixo. A regra Elemental está ligada. Não revelo a mão.", "Gilgamesh is the fixed prize. Elemental is on. I won't reveal the hand.", "Gilgamesh es el premio fijo. La regla Elemental está activa. No revelo la mano.")
					},
					new Line[] {
						N("Grupo dos Trilhos, segunda mesa de {name}! CC Jack!", "Rail Group, {name}'s second table! CC Jack!", "¡Grupo de los Raíles, segunda mesa de {name}! ¡CC Jack!"),
						N("Elemental, Plus e Combo. Três pontos. O Gilgamesh só sai na primeira vitória, fora da mão.", "Elemental, Plus and Combo. Three points. Gilgamesh only comes with the first win, outside the hand.", "Elemental, Plus y Combo. Tres puntos. Gilgamesh solo sale en la primera victoria, fuera de la mano.")
					},
					say("cc_jack", "CC Jack", "CC Jack", "CC Jack",
						"Em Timber o tabuleiro tem elemento. Uma carta de Trovão na casa certa vale mais que um 9 frio. O Gilgamesh observa. Eu também.",
						"In Timber the board has an element. A Thunder card on the right square beats a cold 9. Gilgamesh is watching. So am I.",
						"En Timber el tablero tiene elemento. Una carta de Trueno en la casilla correcta vale más que un 9 frío. Gilgamesh observa. Yo también."),
					say("cc_jack", "CC Jack", "CC Jack", "CC Jack",
						"Você pôs a carta no elemento certo e virou duas. O Gilgamesh troca de mãos. Não chame isso de sorte.",
						"You put the card on the right element and flipped two. Gilgamesh changes hands. Don't call that luck.",
						"Pusiste la carta en el elemento correcto y volteaste dos. Gilgamesh cambia de manos. No lo llames suerte."),
					lost("cc_jack", "CC Jack", "CC Jack", "CC Jack",
						"O elemento virou o jogo contra você. Você jogou um 8 numa casa que não era dele. Senta de novo e olha a casa antes da carta.",
						"The element turned the board against you. You played an 8 on a square that wasn't his. Sit down again and read the square before the card.",
						"El elemento volvió el tablero en tu contra. Jugaste un 8 en una casilla que no era suya. Siéntate otra vez y mira la casilla antes que la carta."),
					new Line[] {
						N("{name} deixa o Jack para trás na tabela! Faltam o sargento Holt e a Rinoa.", "{name} leaves Jack behind in the standings! Sergeant Holt and Rinoa remain.", "¡{name} deja a Jack atrás en la tabla! Faltan el sargento Holt y Rinoa."),
						Q("Elemento não é truque. O Holt não lê casa: ele empurra Plus reto. Não leve um rank baixo para o meio.", "Element is not a trick. Holt doesn't read squares: he shoves Plus straight through. Don't take a low rank into the middle.", "El elemento no es un truco. Holt no lee casillas: empuja Plus en línea recta. No lleves un rango bajo al centro.")
					},
					new Line[] {
						N("Sem pontos contra o Jack. A revanche é nesta mesa, com Elemental. A vaga entre os três primeiros ainda depende disso.", "No points against Jack. The rematch is at this table, with Elemental. A top-three spot still depends on it.", "Sin puntos contra Jack. La revancha es en esta mesa, con Elemental. La plaza entre los tres primeros todavía depende de esto.")
					,
						Q("8 na casa errada. O elemento virou o tabuleiro.",
							"An 8 on the wrong square. The element turned the board.",
							"Un 8 en la casilla equivocada. El elemento volvió el tablero.")
					}),
				duel("soldier_timber", "npc_soldado_galbadia", "Bram Holt", "Bram Holt", "Bram Holt", 5, 7,
					Options.AIType.OFFENSIVE, Ruleset.PLUS, 0, true,
					"Grupo dos Trilhos", "Rail Group", "Grupo de los Raíles",
					"Ofensivo e direto. Sem carta premiada.",
					"Offensive and straight. No prize card.",
					"Ofensivo y directo. Sin carta de premio.",
					new Line[] {
						N("Bram Holt, sargento de folga. O trem parou, o grupo precisava de um nome local, e ele se inscreveu.", "Bram Holt, a sergeant on leave. The train stopped, the group needed a local name, and he signed up.", "Bram Holt, sargento de franco. El tren paró, el grupo necesitaba un nombre local, y él se inscribió."),
						N("Não está aqui a serviço de ninguém. Quer jogar rápido e voltar para o turno. Não tem carta especial.", "He isn't here in anyone's service. He wants to play fast and get back to his shift. He has no special card.", "No está aquí al servicio de nadie. Quiere jugar rápido y volver al turno. No tiene carta especial."),
						N("A mão dele é comum. {name} escolhe uma se vencer. Só isso, e três pontos.", "His hand is ordinary. {name} picks one card on a win. That's all, plus three points.", "Su mano es común. {name} elige una si gana. Solo eso, y tres puntos.")
					},
					new Line[] {
						N("Grupo dos Trilhos! Bram Holt contra {name}!", "Rail Group! Bram Holt against {name}!", "¡Grupo de los Raíles! ¡Bram Holt contra {name}!"),
						N("Ofensivo, Plus. Não há prêmio fixo. Há três pontos e uma carta da mão, se a mesa for sua.", "Offensive, Plus. There is no fixed prize. There are three points and one card from the hand, if the table is yours.", "Ofensivo, Plus. No hay premio fijo. Hay tres puntos y una carta de la mano, si la mesa es tuya.")
					},
					say("npc_soldado_galbadia", "Bram Holt", "Bram Holt", "Bram Holt",
						"Folga curta. Eu jogo Plus reto, sem floreio. Se você deixar um lado somando o mesmo, eu viro. Se eu perder, o turno me espera do mesmo jeito.",
						"Short leave. I play Plus straight, no flourish. If you leave two sides adding to the same, I flip them. If I lose, my shift still waits.",
						"Folga corta. Juego Plus directo, sin florituras. Si dejas dos lados sumando lo mismo, los volteo. Si pierdo, el turno me espera igual."),
					say("npc_soldado_galbadia", "Bram Holt", "Bram Holt", "Bram Holt",
						"Você segurou o meio que eu queria. Pode seguir. Ninguém me mandou esconder carta. Escolha bem na mão: a Rinoa lê elemento.",
						"You held the center I wanted. Move on. Nobody told me to hide a card. Pick carefully from the hand: Rinoa reads element.",
						"Aguantaste el centro que yo quería. Sigue. Nadie me mandó esconder carta. Elige bien de la mano: Rinoa lee el elemento."),
					lost("npc_soldado_galbadia", "Bram Holt", "Bram Holt", "Bram Holt",
						"Recolhe isso. Você abriu o centro e eu fechei o Plus. Ainda tenho folga para uma revanche.",
						"Pick that up. You opened the center and I closed Plus. I've still got leave enough for a rematch.",
						"Recoge eso. Abriste el centro y yo cerré el Plus. Aún me queda folga para la revancha."),
					new Line[] {
						N("Holt soma zero contra {name}! A última do grupo é a Rinoa, dos Forest Owls.", "Holt scores zero against {name}! Last in the group is Rinoa, of the Forest Owls.", "¡Holt suma cero contra {name}! La última del grupo es Rinoa, de los Forest Owls."),
						Q("O Holt joga reto. A Rinoa joga com as pessoas e com o elemento. Trate o nome dela com respeito. Siren não sai de graça.", "Holt plays it straight. Rinoa plays the people and the element. Treat her name with respect. Siren doesn't leave for free.", "Holt juega directo. Rinoa juega con la gente y con el elemento. Trata su nombre con respeto. Siren no se va gratis.")
					},
					new Line[] {
						N("Holt não pontuou contra você, e você não pontuou contra ele. Revanche. Os três primeiros ainda não são seus.", "Holt didn't score against you, and you didn't score against him. Rematch. A top-three spot isn't yours yet.", "Holt no marcó contra ti, y tú no marcaste contra él. Revancha. Los tres primeros todavía no son tuyos.")
					,
						Q("Você abriu o centro. Ele só precisava somar.",
							"You opened the center. He only had to add it up.",
							"Abriste el centro. Él solo tenía que sumar.")
					}),
				duel("rinoa", "rinoa", "Rinoa", "Rinoa", "Rinoa", 5, 7,
					Options.AIType.BALANCED, Ruleset.ELEMENT, 86, true,
					"Grupo dos Trilhos", "Rail Group", "Grupo de los Raíles",
					"Elemental equilibrado. Pressiona sem gritar.",
					"Balanced Elemental. She applies pressure without shouting.",
					"Elemental equilibrado. Presiona sin gritar.",
					new Line[] {
						N("Rinoa Heartilly lidera os Forest Owls, o círculo de cartas da estação de Timber. Não é um destacamento militar.", "Rinoa Heartilly leads the Forest Owls, the card circle at Timber station. It isn't a military squad.", "Rinoa Heartilly lidera a los Forest Owls, el círculo de cartas de la estación de Timber. No es un destacamento militar."),
						N("Quer o nome de Timber entre os três primeiros. Com {name}, o tom vira respeito se a mesa for limpa.", "She wants Timber's name in the top three. With {name}, the tone turns to respect if the game stays clean.", "Quiere el nombre de Timber entre los tres primeros. Con {name}, el tono se vuelve respeto si la mesa es limpia."),
						N("A Siren é o prêmio fixo. Fecha a sua campanha no Grupo dos Trilhos.", "Siren is the fixed prize. It closes out your Rail Group run.", "Siren es el premio fijo. Cierra tu paso por el Grupo de los Raíles.")
					},
					new Line[] {
						N("Última mesa de {name} no Grupo dos Trilhos! Rinoa, dos Forest Owls!", "{name}'s last table in the Rail Group! Rinoa, of the Forest Owls!", "¡Última mesa de {name} en el Grupo de los Raíles! ¡Rinoa, de los Forest Owls!"),
						N("Elemental. Três pontos. O corte desta fase é o terceiro lugar. A Siren sai na primeira vitória.", "Elemental. Three points. This stage's cutoff is third place. Siren comes with the first win.", "Elemental. Tres puntos. El corte de esta fase es el tercer lugar. Siren sale en la primera victoria.")
					},
					say("rinoa", "Rinoa", "Rinoa", "Rinoa",
						"Os Owls querem uma carta que imponha respeito. A Siren pode ser essa. Elemental ligado: eu pressiono a casa certa, sem gritar. E eu quero os 3 pontos, {name}.",
						"The Owls want a card that commands respect. Siren could be that card. Elemental is on: I pressure the right square, without shouting. And I want the 3 points, {name}.",
						"Los Owls quieren una carta que imponga respeto. Siren puede ser esa. Elemental está activo: presiono la casilla correcta, sin gritar. Y quiero los 3 puntos, {name}."),
					say("rinoa", "Rinoa", "Rinoa", "Rinoa",
						"Você leu o elemento no lance que importava. A Siren fica com você. Não precisa contar ao Seifer. Eu mesma não vou contar.",
						"You read the element on the play that mattered. Siren stays with you. You don't have to tell Seifer. I won't tell him either.",
						"Leíste el elemento en la jugada que importaba. Siren se queda contigo. No hace falta contárselo a Seifer. Yo tampoco se lo voy a contar."),
					lost("rinoa", "Rinoa", "Rinoa", "Rinoa",
						"Quase. Você pôs a Siren... quer dizer, a sua carta, na casa errada. A gente também treina em cima dos trilhos. Senta de novo.",
						"Almost. You put your card on the wrong square. We train on the rails too. Sit down again.",
						"Casi. Pusiste tu carta en la casilla equivocada. Nosotras también entrenamos sobre los raíles. Siéntate otra vez."),
					new Line[] {
						N("Grupo dos Trilhos encerrado para {name}! Olhe o quadro: passam três. A fase principal é em Deling, com outro sorteio.", "The Rail Group is closed for {name}! Check the board: three go through. The main stage is in Deling, with a new draw.", "¡Grupo de los Raíles cerrado para {name}! Mira el cuadro: pasan tres. La fase principal es en Deling, con otro sorteo."),
						Q("Ela leu o tabuleiro até o fim. Em Deling o elemento continua, e a Rainha volta para a sua chave. Não leve esta mão para lá sem olhar as casas.", "She read the board to the end. In Deling the element stays on, and the Queen returns to your bracket. Don't take this hand there without reading the squares.", "Leyó el tablero hasta el final. En Deling el elemento sigue, y la Reina vuelve a tu llave. No lleves esta mano allá sin mirar las casillas.")
					},
					new Line[] {
						N("Sem este ponto, a conta do grupo muda. Revanche contra a Rinoa. A derrota não elimina, mas também não classifica.", "Without these points, the group math changes. Rematch against Rinoa. A loss doesn't eliminate you, but it doesn't qualify you either.", "Sin estos puntos, la cuenta del grupo cambia. Revancha contra Rinoa. La derrota no elimina, pero tampoco clasifica.")
					,
						Q("A casa estava marcada. Você pôs a carta na outra.",
							"The square was marked. You put the card on the other one.",
							"La casilla estaba marcada. Pusiste la carta en la otra.")
					})
			});
	}

	private static Region deling() {
		return region("deling", "chegada_deling", "mesa_deling.png", Kind.GROUP, 2,
			new String[][] {{"queen_deling", "diamond"}, {"queen_deling", "soldier_deling"}, {"diamond", "soldier_deling"}},
			"Deling - Fase principal", "Deling - Main stage", "Deling - Fase principal",
			new Line[] {
				N("Timber mandou os três primeiros de cada grupo para a fase principal. {name} passou. Eu conferi o quadro, não um boato.", "Timber sent each group's top three to the main stage. {name} made it. I checked the board, not a rumor.", "Timber mandó a los tres primeros de cada grupo a la fase principal. {name} pasó. Yo revisé el cuadro, no un rumor."),
				N("Deling é a sede porque o salão antigo comporta o público. É prestígio da cidade, não desfile.", "Deling hosts it because the old hall holds the crowd. It's civic prestige, not a parade.", "Deling es la sede porque en el salón antiguo cabe el público. Es prestigio de la ciudad, no un desfile."),
				Q("O novo sorteio te pôs com a Diamond, a Mara Quill e a Rainha. Elemental continua. A Diamond não força a primeira carta. Não force a sua.", "The new draw put you with Diamond, Mara Quill and the Queen. Elemental stays on. Diamond never forces the first card. Don't force yours.", "El nuevo sorteo te puso con Diamond, Mara Quill y la Reina. Elemental sigue. Diamond no fuerza la primera carta. No fuerces la tuya."),
				N("Disputa de posição! Três pontos por vitória. Os dois primeiros deste grupo levam a melhor entrada para Dollet.", "Placement round! Three points per win. This group's top two get the better entry into Dollet.", "¡Disputa de puesto! Tres puntos por victoria. Los dos primeros de este grupo se llevan la mejor entrada a Dollet."),
				say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
					"Meu grupo em Timber também se classificou. {name}, aqui a gente briga pela posição. Elemental no salão: a casa certa vale mais que a carta alta na casa errada.",
					"My Timber group qualified too. {name}, here we fight for placement. Elemental in the salon: the right square beats a high card on the wrong one.",
					"Mi grupo en Timber también se clasificó. {name}, aquí se pelea la posición. Elemental en el salón: la casilla correcta vale más que la carta alta en la equivocada.")
			},
			new Duel[] {
				duel("diamond", "cc_diamond", "CC Diamond", "CC Diamond", "CC Diamond", 5, 7,
					Options.AIType.BALANCED, Ruleset.ELEMENT, 87, true,
					"Disputa de posição", "Placement match", "Disputa de puesto",
					"Etiqueta e Elemental. Não força a primeira carta.",
					"Manners and Elemental. She never forces the first card.",
					"Etiqueta y Elemental. No fuerza la primera carta.",
					new Line[] {
						N("CC Diamond joga no salão de Deling. Não é o Jack: o Jack lê o tabuleiro, a Diamond lê o ambiente.", "CC Diamond plays in the Deling salon. She isn't Jack: Jack reads the board, Diamond reads the room.", "CC Diamond juega en el salón de Deling. No es Jack: Jack lee el tablero, Diamond lee el ambiente."),
						N("Entrou para a cidade ter um nome que sabe se sentar sem fazer cena. Quer a melhor chave, não só passar.", "She entered so the city would have a player who can sit down without making a scene. She wants the better bracket, not just to go through.", "Entró para que la ciudad tenga un nombre que sabe sentarse sin armar escena. Quiere la mejor llave, no solo pasar."),
						N("Sacred é o prêmio fixo. Três pontos na disputa de posição.", "Sacred is the fixed prize. Three points in the placement round.", "Sacred es el premio fijo. Tres puntos en la disputa de puesto.")
					},
					new Line[] {
						N("Deling, fase principal! CC Diamond contra {name}!", "Deling, main stage! CC Diamond against {name}!", "¡Deling, fase principal! ¡CC Diamond contra {name}!"),
						N("Elemental, jogo contido. Em jogo, a posição: os dois primeiros vão a Dollet com a melhor entrada.", "Elemental, a measured game. At stake is placement: the top two go to Dollet with the better entry.", "Elemental, juego contenido. En juego, el puesto: los dos primeros van a Dollet con la mejor entrada.")
					},
					say("cc_diamond", "CC Diamond", "CC Diamond", "CC Diamond",
						"Em Deling, ninguém se senta sem convite. O seu chegou. Elemental, etiqueta. Eu não forço a primeira carta. Sacred continua comigo até a mesa dizer o contrário.",
						"In Deling, no one sits without an invitation. Yours arrived. Elemental, manners. I never force the first card. Sacred stays with me until the table says otherwise.",
						"En Deling, nadie se sienta sin invitación. La tuya llegó. Elemental, etiqueta. No fuerzo la primera carta. Sacred sigue conmigo hasta que la mesa diga lo contrario."),
					say("cc_diamond", "CC Diamond", "CC Diamond", "CC Diamond",
						"Bonito. Você esperou a casa certa e virou Sacred sem escândalo. A posição, essa eu vou ter de reconquistar na soma.",
						"Clean. You waited for the right square and flipped Sacred without a scene. The placement, I'll have to win back on the sums.",
						"Limpio. Esperaste la casilla correcta y volteaste a Sacred sin escándalo. La posición tendré que recuperarla en las sumas."),
					lost("cc_diamond", "CC Diamond", "CC Diamond", "CC Diamond",
						"Faltou classe na última carta: você forçou um 9 numa casa que não era dele. Outra vez, com a mesma etiqueta.",
						"That last card lacked manners: you forced a 9 onto a square that wasn't his. Again, same etiquette.",
						"Faltó clase en la última carta: forzaste un 9 en una casilla que no era suya. Otra vez, con la misma etiqueta."),
					new Line[] {
						N("Três pontos em Deling! A Diamond continua no grupo até o corte. A próxima é Mara Quill.", "Three points in Deling! Diamond stays in the group until the cutoff. Next is Mara Quill.", "¡Tres puntos en Deling! Diamond sigue en el grupo hasta el corte. La siguiente es Mara Quill."),
						Q("Salão não ganha partida. A casa certa ganha. A Quill organiza a noite e joga ofensivo. Não trate o Minotauro como enfeite.", "A salon doesn't win a match. The right square does. Quill runs the night and plays on the attack. Don't treat Minotaur like a decoration.", "El salón no gana la partida. La casilla correcta sí. Quill organiza la noche y juega al ataque. No trates a Minotauro como adorno.")
					},
					new Line[] {
						N("Posição não muda com derrota. Revanche contra a Diamond. O corte dos dois primeiros pede pontos de verdade.", "Placement doesn't change on a loss. Rematch against Diamond. The top-two cutoff needs real points.", "El puesto no cambia con una derrota. Revancha contra Diamond. El corte de los dos primeros pide puntos de verdad.")
					,
						Q("Você forçou o 9. Ela espera a casa. Por isso ganhou.",
							"You forced the 9. She waits for the square. That's why she won.",
							"Forzaste el 9. Ella espera la casilla. Por eso ganó.")
					}),
				duel("soldier_deling", "npc_soldado_galbadia", "Mara Quill", "Mara Quill", "Mara Quill", 6, 7,
					Options.AIType.OFFENSIVE, Ruleset.ELEMENT, 88, true,
					"Disputa de posição", "Placement match", "Disputa de puesto",
					"Ofensiva formal. Elemental nas mãos de quem organiza a noite.",
					"Formal and offensive. Elemental, from the woman who runs the salon's card night.",
					"Ofensiva y formal. Elemental, en manos de quien organiza la noche del salón.",
					new Line[] {
						N("Mara Quill, capitã, organiza a noite de cartas do salão. Não é o Bram Holt: outra pessoa, outra cidade, a mesma farda galbadiana.", "Mara Quill, a captain, runs the salon's card night. She isn't Bram Holt: another person, another city, the same Galbadian uniform.", "Mara Quill, capitana, organiza la noche de cartas del salón. No es Bram Holt: otra persona, otra ciudad, el mismo uniforme galbadiano."),
						N("Joga pela casa. Quer Deling no corte, não numa história de quartel.", "She plays for the house. She wants Deling in the cutoff, not in a barracks story.", "Juega por la casa. Quiere a Deling en el corte, no en una historia de cuartel."),
						N("O Minotauro é o prêmio fixo desta mesa.", "Minotaur is this table's fixed prize.", "Minotauro es el premio fijo de esta mesa.")
					},
					new Line[] {
						N("Fase principal, Deling! Mara Quill contra {name}!", "Main stage, Deling! Mara Quill against {name}!", "¡Fase principal, Deling! ¡Mara Quill contra {name}!"),
						N("Elemental ofensivo. Três pontos de posição. O Minotauro não está na mão comum.", "Offensive Elemental. Three placement points. Minotaur isn't in the regular hand.", "Elemental ofensivo. Tres puntos de puesto. Minotauro no está en la mano común.")
					},
					say("npc_soldado_galbadia", "Mara Quill", "Mara Quill", "Mara Quill",
						"Eu organizo a noite e também me sento. Elemental, no ataque. O Minotauro fica guardado até alguém merecer a casa em que ele entra.",
						"I run the night and I sit down too. Elemental, on the attack. Minotaur stays put until someone earns the square he enters.",
						"Organizo la noche y también me siento. Elemental, al ataque. Minotauro sigue guardado hasta que alguien merezca la casilla en la que entra."),
					say("npc_soldado_galbadia", "Mara Quill", "Mara Quill", "Mara Quill",
						"Você segurou o ataque no elemento certo. Toma o Minotauro. Não fique exibindo isso no corredor. O que importa é a tabela.",
						"You held off the attack on the right element. Take Minotaur. Don't show it off in the hall. The table is what matters.",
						"Aguantaste el ataque en el elemento correcto. Toma a Minotauro. No lo exhibas en el pasillo. Lo que importa es la tabla."),
					lost("npc_soldado_galbadia", "Mara Quill", "Mara Quill", "Mara Quill",
						"Uma carta não muda o salão. Você abriu o ataque numa casa neutra e eu completei o Elemental. De novo, com a mesma regra.",
						"One card doesn't change the salon. You opened the attack on a neutral square and I finished the Elemental. Again, same rule.",
						"Una carta no cambia el salón. Abriste el ataque en una casilla neutra y yo cerré el Elemental. Otra vez, con la misma regla."),
					new Line[] {
						N("{name} pontua no grupo de Deling! Falta a Rainha: é a briga direta pelo primeiro lugar.", "{name} scores in the Deling group! The Queen is left: it's a straight fight for first place.", "¡{name} puntúa en el grupo de Deling! Falta la Reina: es la pelea directa por el primer puesto."),
						Q("A Quill joga sério. A Rainha joga como quem já ganhou isto uma vez: ela puxa o Elemental para o canto dela. Não é a mesma mesa.", "Quill plays it seriously. The Queen plays like someone who has already won this once: she pulls Elemental onto her corner. It's not the same table.", "Quill juega en serio. La Reina juega como quien ya ganó esto una vez: lleva el Elemental a su esquina. No es la misma mesa.")
					},
					new Line[] {
						N("Sem pontos contra a Quill. Revanche. Disputa de posição não perdoa conta pela metade.", "No points against Quill. Rematch. A placement round doesn't forgive half-done math.", "Sin punto contra Quill. Revancha. La disputa de puesto no perdona una cuenta a medias.")
					,
						Q("Ataque em casa neutra. Ela trouxe o Elemental.",
							"Attack on a neutral square. She brought the Elemental.",
							"Ataque en casilla neutra. Ella trajo el Elemental.")
					}),
				duel("queen_deling", "card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas", 6, 7,
					Options.AIType.OFFENSIVE, Ruleset.ELEMENT, 95, true,
					"Disputa de posição", "Placement match", "Disputa de puesto",
					"Ofensiva elegante. Elemental a favor do canto dela.",
					"Elegant and offensive. Elemental on her corner.",
					"Ofensiva y elegante. Elemental a favor de su esquina.",
					new Line[] {
						N("A Rainha voltou ao grupo de {name} porque os classificados de Timber foram sorteados de novo.", "The Queen is back in {name}'s group because Timber's qualifiers were drawn again.", "La Reina volvió al grupo de {name} porque los clasificados de Timber se sortearon de nuevo."),
						N("Ela já não trata {name} como estreante. Quer a melhor entrada em Dollet.", "She no longer treats {name} as a newcomer. She wants the best entry into Dollet.", "Ya no trata a {name} como debutante. Quiere la mejor entrada a Dollet."),
						N("O Alexander é o prêmio fixo. Não dá para escolhê-lo na mão. Só na primeira vitória.", "Alexander is the fixed prize. You can't choose him from the hand. First win only.", "Alexander es el premio fijo. No se puede elegir de la mano. Solo en la primera victoria.")
					},
					new Line[] {
						N("Deling, jogo da posição! A Rainha das Cartas contra {name}!", "Deling, the placement decider! The Queen of Cards against {name}!", "¡Deling, partido de puesto! ¡La Reina de las Cartas contra {name}!"),
						N("Elemental. Três pontos que podem definir o primeiro lugar. Os dois primeiros seguem para a rodada decisiva, em Dollet.", "Elemental. Three points that can decide first place. The top two go to the decisive round, in Dollet.", "Elemental. Tres puntos que pueden definir el primer lugar. Los dos primeros siguen a la ronda decisiva, en Dollet.")
					},
					say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"{name}. O Alexander passou por este salão. Elemental a favor do meu canto. Se você virar esse canto, ele vai na bagagem. O primeiro lugar, eu ainda quero.",
						"{name}. Alexander has been through this salon. Elemental favors my corner. If you flip that corner, he goes in the bag. I still want first place.",
						"{name}. Alexander pasó por este salón. Elemental a favor de mi esquina. Si volteas esa esquina, se va en el equipaje. El primer puesto, aún lo quiero."),
					say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"Você me tirou o canto com o elemento contra mim. Leva o Alexander. Você fica na frente neste grupo. Em Dollet a conta começa de novo, e o Diablos já está na minha mão.",
						"You took my corner with the element against me. Take Alexander. You sit first in this group. In Dollet the count starts over, and Diablos is already in my hand.",
						"Me quitaste la esquina con el elemento en mi contra. Llévate a Alexander. Quedas primero en este grupo. En Dollet la cuenta empieza de nuevo, y Diablos ya está en mi mano."),
					lost("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"O Alexander fica. Você quase fechou o canto e parou um lance cedo. Posição não se ganha no quase.",
						"Alexander stays. You almost closed the corner and stopped a play early. Placement isn't won on an almost.",
						"Alexander se queda. Casi cerraste la esquina y te paraste una jugada antes. La posición no se gana en el casi."),
					new Line[] {
						N("Confira a tabela de Deling. O corte é o segundo lugar. Quem passa vai a Dollet confirmar a vaga no pub.", "Check the Deling standings. The cutoff is second place. Whoever goes through heads to Dollet to confirm their spot at the pub.", "Revisa la tabla de Deling. El corte es el segundo lugar. Quien pase va a Dollet a confirmar la plaza en el pub."),
						Q("Dollet decide o mata-mata. O pub é só o endereço. Tomas defende a própria mesa. Não trate Chubby Chocobo como piada de taberna.", "Dollet decides the bracket. The pub is just the address. Tomas defends his own table. Don't treat Chubby Chocobo like a tavern joke.", "Dollet decide el cuadro. El pub es solo la dirección. Tomas defiende su propia mesa. No trates a Chubby Chocobo como chiste de taberna.")
					},
					new Line[] {
						N("A Rainha segurou os pontos. Revanche aqui. Sem a vitória, a posição que você quer não existe.", "The Queen held on to the points. Rematch here. Without the win, the place you want doesn't exist.", "La Reina retuvo los puntos. Revancha aquí. Sin la victoria, el puesto que quieres no existe.")
					,
						Q("Você parou um lance cedo. O canto era dela.",
							"You stopped a play early. The corner was hers.",
							"Te paraste una jugada antes. La esquina era suya.")
					})
			});
	}

	private static Region dollet() {
		return region("dollet", "chegada_dollet", "mesa_dollet.png", Kind.GROUP, 2,
			new String[][] {{"queen_dollet", "pub"}, {"queen_dollet", "soldier_dollet"}, {"pub", "soldier_dollet"}},
			"Dollet - Rodada decisiva", "Dollet - Decisive round", "Dollet - Ronda decisiva",
			new Line[] {
				N("Deling mandou os dois primeiros. {name} está entre eles. O outro classificado do seu grupo também.", "Deling sent the top two. {name} is one of them. So is the other qualifier from your group.", "Deling mandó a los dos primeros. {name} está entre ellos. El otro clasificado de tu grupo también."),
				N("Dollet sedia a rodada decisiva porque o segundo andar do pub recebe partidas de cartas há anos. A comissão alugou a sala, não inventou uma guerra.", "Dollet hosts the decisive round because the pub's second floor has hosted card games for years. The committee rented the room. It didn't invent a war.", "Dollet acoge la ronda decisiva porque el segundo piso del pub recibe partidas de cartas desde hace años. La comisión alquiló la sala, no inventó una guerra."),
				Q("Tomas é o dono da mesa, e ele joga defensivo. A Doss lê a praça. A Rainha veio confirmar o primeiro lugar com Diablos. Não durma no Plus do pub.", "Tomas owns the table, and he plays defense. Doss reads the square. The Queen came to lock first place with Diablos. Don't doze off on the pub's Plus.", "Tomas es el dueño de la mesa, y juega a la defensiva. Doss lee la plaza. La Reina vino a confirmar el primer puesto con Diablos. No te duermas en el Plus del pub."),
				N("Todos contra todos! Três pontos por vitória. Os dois primeiros entram no mata-mata em lados opostos: o primeiro vai a FH, o segundo à outra quarta de final.", "Round robin! Three points per win. The top two enter the knockouts on opposite sides: first goes to FH, second to the other quarterfinal.", "¡Todos contra todos! Tres puntos por victoria. Los dos primeros entran en las eliminatorias en lados opuestos: el primero va a FH, el segundo al otro cuarto de final."),
				say("npc_pub_dollet", "Tomas Reed", "Tomas Reed", "Tomas Reed",
					"Senta. O Chubby Chocobo é a aposta da casa, não o lanche. Plus, no meu tempo. Eu quero os 3 pontos tanto quanto vocês.",
					"Sit. Chubby Chocobo is the house bet, not a snack. Plus, at my pace. I want the 3 points as much as you do.",
					"Siéntate. Chubby Chocobo es la apuesta de la casa, no el tentempié. Plus, a mi ritmo. Quiero los 3 puntos tanto como vosotros.")
			},
			new Duel[] {
				duel("pub", "npc_pub_dollet", "Tomas Reed", "Tomas Reed", "Tomas Reed", 4, 6,
					Options.AIType.DEFENSIVE, Ruleset.PLUS, 78, true,
					"Rodada decisiva", "Decisive round", "Ronda decisiva",
					"Defensivo, de quem conhece a própria mesa.",
					"Defensive, from the man who knows his own table.",
					"Defensivo, de quien conoce su propia mesa.",
					new Line[] {
						N("Tomas Reed é a terceira geração do pub de Dollet. A comissão escolheu a cidade pela sala, e ele se inscreveu para o nome do pub ir junto.", "Tomas Reed is the third generation to run the Dollet pub. The committee chose the city for the room, and he signed up so the pub's name would come along.", "Tomas Reed es la tercera generación del pub de Dollet. La comisión eligió la ciudad por la sala, y él se inscribió para que el nombre del pub fuera junto."),
						N("Joga calmo, no próprio território. Não é soldado e não finge ser.", "He plays calm, on his own ground. He isn't a soldier and he doesn't pretend to be.", "Juega tranquilo, en su propio terreno. No es soldado y no finge serlo."),
						N("O Chubby Chocobo é o prêmio fixo. Três pontos na rodada que define o mata-mata.", "Chubby Chocobo is the fixed prize. Three points in the round that sets the knockouts.", "Chubby Chocobo es el premio fijo. Tres puntos en la ronda que define las eliminatorias.")
					},
					new Line[] {
						N("Dollet, rodada decisiva, Grupo do Pub! Tomas Reed contra {name}!", "Dollet, decisive round, Pub Group! Tomas Reed against {name}!", "¡Dollet, ronda decisiva, Grupo del Pub! ¡Tomas Reed contra {name}!"),
						N("Plus defensivo. Três pontos. Classificam-se dois, e eles não caem no mesmo lado do mata-mata.", "Defensive Plus. Three points. Two qualify, and they land on opposite sides of the knockouts.", "Plus defensivo. Tres puntos. Se clasifican dos, y no caen en el mismo lado de las eliminatorias.")
					},
					say("npc_pub_dollet", "Tomas Reed", "Tomas Reed", "Tomas Reed",
						"Partida tranquila, regra dura. Eu seguro o canto e espero o seu Plus furar. Se você virar a parede, o Chubby sai com você.",
						"Quiet match, hard rule. I'll hold the corner and wait for your Plus to miss. If you flip that wall, Chubby leaves with you.",
						"Partida tranquila, regla dura. Aguanto la esquina y espero que tu Plus falle. Si volteas esa pared, Chubby se va contigo."),
					say("npc_pub_dollet", "Tomas Reed", "Tomas Reed", "Tomas Reed",
						"Servido. Você teve paciência no canto. O Chubby vai com você. A Enne, lá fora, joga Elemental e não tem o meu bom humor.",
						"Served. You had patience on the corner. Chubby goes with you. Enne, outside, plays Elemental and hasn't got my good humor.",
						"Servido. Tuviste paciencia en la esquina. Chubby se va contigo. Enne, ahí fuera, juega Elemental y no tiene mi buen humor."),
					lost("npc_pub_dollet", "Tomas Reed", "Tomas Reed", "Tomas Reed",
						"A casa fica com esta. Você soltou o Plus cedo demais e eu recuei o canto. A próxima, eu sirvo na mesma mesa.",
						"The house keeps this one. You dropped Plus too early and I pulled the corner back. Next one, I serve at the same table.",
						"La casa se queda con esta. Soltaste el Plus demasiado pronto y yo retiré la esquina. La siguiente la sirvo en la misma mesa."),
					new Line[] {
						N("Três pontos no pub! A vaga de {name} ainda depende dos outros dois jogos.", "Three points at the pub. {name}'s spot still depends on the other two matches.", "Tres puntos en el pub. La plaza de {name} todavía depende de los otros dos partidos."),
						Q("Não se embriague com a vitória. A Doss defende a praça com Elemental. MiniMog só sai se você ler a casa, não o humor do pub.", "Don't get drunk on the win. Doss defends the square with Elemental. MiniMog only leaves if you read the square, not the pub's mood.", "No te emborraches con la victoria. Doss defiende la plaza con Elemental. MiniMog solo se va si lees la casilla, no el humor del pub.")
					},
					new Line[] {
						N("O pub não cedeu os pontos. Revanche contra o Tomas. Rodada decisiva sem pontos é só conversa.", "The pub didn't give up the points. Rematch against Tomas. A decisive round without points is just talk.", "El pub no cedió los puntos. Revancha contra Tomas. Una ronda decisiva sin puntos es solo charla.")
					,
						Q("Plus cedo demais. Ele só recua o canto e espera.",
							"Plus too early. He just pulls the corner back and waits.",
							"Plus demasiado pronto. Solo retira la esquina y espera.")
					}),
				duel("soldier_dollet", "npc_soldado_dollet", "Enne Doss", "Enne Doss", "Enne Doss", 5, 7,
					Options.AIType.BALANCED, Ruleset.ELEMENT, 81, true,
					"Rodada decisiva", "Decisive round", "Ronda decisiva",
					"Equilibrada. Elemental, orgulho da praça.",
					"Balanced. Elemental, the pride of the square.",
					"Equilibrada. Elemental, orgullo de la plaza.",
					new Line[] {
						N("Enne Doss, cabo da guarnição de Dollet. Joga na praça depois do turno e se inscreveu quando a fase chegou à cidade.", "Enne Doss, a corporal in the Dollet garrison. She plays in the square after her shift and signed up when the phase came to the city.", "Enne Doss, cabo de la guarnición de Dollet. Juega en la plaza después del turno y se inscribió cuando la fase llegó a la ciudad."),
						N("Quer que a praça tenha um nome no corte. Justa, e teimosa com a própria mão.", "She wants the square to have a name in the cutoff. Fair, and stubborn with her own hand.", "Quiere que la plaza tenga un nombre en el corte. Justa, y terca con su propia mano."),
						N("O MiniMog é o prêmio fixo desta mesa.", "MiniMog is this table's fixed prize.", "MiniMog es el premio fijo de esta mesa.")
					},
					new Line[] {
						N("Rodada decisiva! Enne Doss contra {name}, com a praça lá fora!", "Decisive round! Enne Doss against {name}, with the square right outside!", "¡Ronda decisiva! ¡Enne Doss contra {name}, con la plaza ahí fuera!"),
						N("Elemental. Três pontos. O MiniMog só sai na primeira vitória, fora da escolha comum.", "Elemental. Three points. MiniMog only comes with the first win, apart from the regular pick.", "Elemental. Tres puntos. MiniMog solo sale en la primera victoria, aparte de la elección común.")
					},
					say("npc_soldado_dollet", "Enne Doss", "Enne Doss", "Enne Doss",
						"A praça é a nossa casa, não uma trincheira. Elemental, jogo equilibrado. O MiniMog só sai numa vitória limpa, na casa certa.",
						"The square is our house, not a trench. Elemental, a balanced game. MiniMog only leaves on a clean win, on the right square.",
						"La plaza es nuestra casa, no una trinchera. Elemental, juego equilibrado. MiniMog solo se va con una victoria limpia, en la casilla correcta."),
					say("npc_soldado_dollet", "Enne Doss", "Enne Doss", "Enne Doss",
						"Pode ir. Você respeitou a casa e virou o MiniMog. A tabela que decida o resto. A Rainha, no pub, não vai facilitar o primeiro lugar.",
						"You can go. You respected the square and flipped MiniMog. Let the table decide the rest. The Queen, back at the pub, won't hand over first place.",
						"Puedes irte. Respetaste la casilla y volteaste a MiniMog. Que la tabla decida el resto. La Reina, en el pub, no va a regalar el primer puesto."),
					lost("npc_soldado_dollet", "Enne Doss", "Enne Doss", "Enne Doss",
						"A praça continua no mesmo lugar. Você ignorou o elemento e eu fiquei com a casa. Arruma a mão e volta.",
						"The square is still right here. You ignored the element and I kept the house. Fix your hand and come back.",
						"La plaza sigue en el mismo sitio. Ignoraste el elemento y me quedé con la casilla. Arregla la mano y vuelve."),
					new Line[] {
						N("{name} deixa a Doss sem pontos neste confronto! A última mesa em Dollet é a da Rainha.", "{name} leaves Doss without points in this match! The last table in Dollet is the Queen's.", "¡{name} deja a Doss sin puntos en este enfrentamiento! La última mesa en Dollet es la de la Reina."),
						Q("Se vocês dois passarem, ficam em lados opostos do mata-mata. Não facilite a última carta para resolver logo. Leia a casa.", "If you both go through, you'll be on opposite sides of the bracket. Don't dump the last card just to end it. Read the square.", "Si los dos pasan, quedáis en lados opuestos del cuadro. No sueltes la última carta para resolver pronto. Lee la casilla.")
					},
					new Line[] {
						N("Os pontos da Doss não viraram seus. Revanche. O corte dos dois primeiros só conta vitórias.", "Doss's points didn't become yours. Rematch. The top-two cutoff only counts wins.", "Los puntos de Doss no pasaron a ser tuyos. Revancha. El corte de los dos primeros solo cuenta victorias.")
					,
						Q("Você ignorou o elemento. A praça ficou com ela.",
							"You ignored the element. The square stayed with her.",
							"Ignoraste el elemento. La plaza se quedó con ella.")
					}),
				duel("queen_dollet", "card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas", 6, 8,
					Options.AIType.OFFENSIVE, Ruleset.ELEMENT, 90, true,
					"Rodada decisiva", "Decisive round", "Ronda decisiva",
					"Ofensiva. Ela joga para terminar em primeiro, não para sobreviver.",
					"Offensive. She plays to finish first, not to survive.",
					"Ofensiva. Juega para quedar primera, no para sobrevivir.",
					new Line[] {
						N("A Rainha confirma em Dollet a entrada que trouxe de Deling. Mesmo grupo de {name}, pela última vez antes do mata-mata.", "The Queen confirms in Dollet the entry she brought from Deling. Same group as {name}, one last time before the knockouts.", "La Reina confirma en Dollet la entrada que trajo de Deling. Mismo grupo que {name}, por última vez antes de las eliminatorias."),
						N("Se os dois terminarem no topo, o primeiro vai às quartas em FH e o segundo à outra quarta de final.", "If you both finish on top, first place goes to the quarterfinal at FH and second to the other quarterfinal.", "Si los dos terminan arriba, el primero va a los cuartos en FH y el segundo al otro cuarto de final."),
						N("O Diablos é o prêmio fixo deste encontro. A rivalidade já tem nome.", "Diablos is this meeting's fixed prize. The rivalry already has a name.", "Diablos es el premio fijo de este encuentro. La rivalidad ya tiene nombre.")
					},
					new Line[] {
						N("Dollet, última mesa do grupo! A Rainha das Cartas contra {name}!", "Dollet, the group's final table! The Queen of Cards against {name}!", "¡Dollet, última mesa del grupo! ¡La Reina de las Cartas contra {name}!"),
						N("Elemental ofensivo. Três pontos. Diablos na primeira vitória. O primeiro lugar fica com o lado de FH.", "Offensive Elemental. Three points. Diablos on the first win. First place takes the FH side.", "Elemental ofensivo. Tres puntos. Diablos en la primera victoria. El primer puesto se queda con el lado de FH.")
					},
					say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"Parei no pub porque a fase é aqui. Elemental, no ataque. O Diablos pode mudar de mesa se você virar o canto que eu escolhi. O lado do mata-mata também.",
						"I stopped at the pub because this is the stage. Elemental, on the attack. Diablos can change tables if you flip the corner I chose. So can the side of the bracket.",
						"Paré en el pub porque la fase es aquí. Elemental, al ataque. Diablos puede cambiar de mesa si volteas la esquina que yo elegí. El lado del cuadro también."),
					say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"Você me tirou o canto no elemento. Leva o Diablos. Se a tabela te puser na frente, eu vou para a outra quarta. Não acabou.",
						"You took my corner on the element. Take Diablos. If the table puts you first, I'll go to the other quarterfinal. This isn't over.",
						"Me quitaste la esquina en el elemento. Llévate a Diablos. Si la tabla te pone delante, yo voy al otro cuarto. No se acabó."),
					lost("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"O Diablos prefere ficar. Você abriu o ataque no canto errado e eu completei o Elemental. Primeiro lugar não se herda.",
						"Diablos prefers to stay. You opened the attack on the wrong corner and I finished the Elemental. First place isn't inherited.",
						"Diablos prefiere quedarse. Abriste el ataque en la esquina equivocada y yo cerré el Elemental. El primer puesto no se hereda."),
					new Line[] {
						N("Olhe o Grupo do Pub. Passam dois, em lados opostos. {name} joga as quartas em Fisherman's Horizon se ficar no corte.", "Look at the Pub Group. Two go through, on opposite sides. {name} plays the quarterfinal at Fisherman's Horizon if the cutoff holds.", "Mira el Grupo del Pub. Pasan dos, en lados opuestos. {name} juega los cuartos en Fisherman's Horizon si queda en el corte."),
						Q("Em FH o grupo é curto: uma vaga. Antes, uma mesa à parte contra a Rainha: todas as regras, Same Wall e Morte súbita, sem ponto. Não trate isso como aquecimento.", "At FH the group is short: one spot. Before that, a side table against the Queen: every rule, Same Wall and Sudden Death, no points. Don't treat it as a warm-up.", "En FH el grupo es corto: una plaza. Antes, una mesa aparte contra la Reina: todas las reglas, Same Wall y Muerte súbita, sin puntos. No lo tomes como calentamiento.")
					},
					new Line[] {
						N("A Rainha segurou Dollet nesta mesa. Revanche. Sem os três pontos, o mata-mata não tem o seu nome.", "The Queen held Dollet at this table. Rematch. Without the three points, your name isn't in the knockouts.", "La Reina retuvo Dollet en esta mesa. Revancha. Sin los tres puntos, tu nombre no está en las eliminatorias.")
					,
						Q("Canto errado, Elemental certo para ela. Diablos não sai.",
							"Wrong corner, the right Elemental for her. Diablos doesn't leave.",
							"Esquina equivocada, el Elemental correcto para ella. Diablos no se va.")
					})
			});
	}

	private static Region fh() {
		return region("fh", "chegada_fh", "mesa_fh.png", Kind.GROUP, 1,
			new String[][] {{"flo", "pescador"}, {"flo", "shumi"}, {"shumi", "pescador"}},
			"FH - Quartas", "FH - Quarterfinals", "FH - Cuartos",
			new Line[] {
				N("Dollet fechou o corte. {name} entrou! O primeiro lugar deste lado joga as quartas aqui.", "Dollet set the cutoff. {name} is in! First place on this side plays the quarterfinal here.", "Dollet cerró el corte. ¡{name} entró! El primer puesto de este lado juega los cuartos aquí."),
				N("Fisherman's Horizon é a sede porque a plataforma comporta o público sem tirar o cais do lugar. É a fase da casa, não um cerco.", "Fisherman's Horizon hosts it because the platform fits the crowd without crowding out the dock. It's a home round, not a siege.", "Fisherman's Horizon es la sede porque en la plataforma cabe el público sin desplazar el muelle. Es la fase de casa, no un asedio."),
				Q("Antes do grupo, cabeça de chave contra a Rainha. Todas as regras: Same, Same Wall, Plus, Combo, Elemental e Morte súbita. Não vale ponto. Não elimina. Ainda assim, jogue a parede.", "Before the group, a seed match against the Queen. Every rule: Same, Same Wall, Plus, Combo, Elemental and Sudden Death. It scores nothing. It eliminates no one. Still, play the wall.", "Antes del grupo, cabeza de serie contra la Reina. Todas las reglas: Same, Same Wall, Plus, Combo, Elemental y Muerte súbita. No da puntos. No elimina. Aun así, juega el muro."),
				N("Depois, o Grupo das Quartas: você, Nash Pell, o representante Shumi e Dobe e Flo. Só o primeiro vai à semifinal.", "Then the Quarterfinal Group: you, Nash Pell, the Shumi representative, and Dobe and Flo. Only first place goes to the semifinal.", "Después, el Grupo de los Cuartos: tú, Nash Pell, el representante Shumi y Dobe y Flo. Solo el primero va a la semifinal."),
				say("dobe_flo", "Flo", "Flo", "Flo",
					"FH joga junto. Dobe e eu somos uma inscrição só, no ataque, com Elemental. Pode se sentar, {name}. A casa não vai facilitar o Plus.",
					"FH plays as one. Dobe and I are a single entry, on the attack, with Elemental. You can sit, {name}. The house won't make Plus easy.",
					"FH juega junta. Dobe y yo somos una sola inscripción, al ataque, con Elemental. Puedes sentarte, {name}. La casa no va a facilitar el Plus.")
			},
			new Duel[] {
				duel("queen_fh", "card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas", 7, 7,
					Options.AIType.OFFENSIVE, Ruleset.FINAL, 98, false,
					"Cabeça de chave", "Seeding match", "Cabeza de serie",
					"Todas as regras. Não vale ponto de grupo.",
					"Every rule on. It doesn't score group points.",
					"Todas las reglas. No suma puntos de grupo.",
					new Line[] {
						N("A comissão marcou esta mesa no cais, antes das quartas. Quem vencer entra como cabeça de chave da própria quarta.", "The committee set this table on the dock, before the quarterfinals. The winner enters as the top seed of their quarterfinal.", "La comisión marcó esta mesa en el muelle, antes de los cuartos. Quien gane entra como cabeza de serie de su propio cuarto."),
						N("A Rainha segue do outro lado mesmo se perder. {name} segue nas quartas daqui mesmo se perder. Por isso existe a revanche, e por isso não há pontos.", "The Queen stays on the other side even if she loses. {name} stays in the quarterfinal here even on a loss. That's why there's a rematch, and why there are no points.", "La Reina sigue en el otro lado aunque pierda. {name} sigue en los cuartos de aquí aunque pierda. Por eso existe la revancha, y por eso no hay puntos."),
						N("O Doomtrain é o prêmio fixo. Não entra na mão e não substitui a carta que você escolher.", "Doomtrain is the fixed prize. It doesn't enter the hand and it doesn't replace the card you choose.", "Doomtrain es el premio fijo. No entra en la mano y no sustituye la carta que elijas.")
					},
					new Line[] {
						N("Fisherman's Horizon! Partida de cabeça de chave, fora da tabela. A Rainha contra {name}!", "Fisherman's Horizon! A seeding match, off the standings. The Queen against {name}!", "¡Fisherman's Horizon! Partida de cabeza de serie, fuera de la tabla. ¡La Reina contra {name}!"),
						N("Same, Same Wall, Plus, Combo, Elemental e Morte súbita. Ninguém é eliminado. Doomtrain só na primeira vitória.", "Same, Same Wall, Plus, Combo, Elemental and Sudden Death. Nobody is eliminated. Doomtrain only on the first win.", "Same, Same Wall, Plus, Combo, Elemental y Muerte súbita. Nadie queda eliminado. Doomtrain solo en la primera victoria.")
					},
					say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"Isto não me tira da outra quarta e não te tira da sua. Ainda assim, eu quero ser cabeça de chave, {name}. Same Wall e Morte súbita estão ligados. O Doomtrain observa a parede.",
						"This doesn't take me out of the other quarter or you out of yours. Still, I want the seed, {name}. Same Wall and Sudden Death are on. Doomtrain is watching the wall.",
						"Esto no me saca del otro cuarto ni a ti del tuyo. Aun así, quiero ser cabeza de serie, {name}. Same Wall y Muerte súbita están activos. Doomtrain observa el muro."),
					say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"Você usou a parede contra mim e segurou a Morte súbita. Leva o Doomtrain. Eu vou jogar a minha quarta. Se cada lado vencer, a final já tem nome.",
						"You used the wall against me and held through Sudden Death. Take Doomtrain. I'll play my quarterfinal. If each side wins, the final already has a name.",
						"Usaste el muro en mi contra y aguantaste la Muerte súbita. Llévate a Doomtrain. Yo juego mi cuarto. Si cada lado gana, la final ya tiene nombre."),
					lost("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"A cabeça de chave fica comigo. Você esqueceu a parede no último Same, e a Morte súbita me devolveu a casa. O trem não levou ninguém embora do torneio.",
						"The seed stays with me. You forgot the wall on the last Same, and Sudden Death gave me the square back. The train didn't take anyone out of the tournament.",
						"La cabeza de serie se queda conmigo. Olvidaste el muro en el último Same, y la Muerte súbita me devolvió la casilla. El tren no se llevó a nadie del torneo."),
					new Line[] {
						N("Cabeça de chave para {name}! A Rainha permanece na outra quarta. Agora, a tabela das quartas: só o primeiro passa.", "Top seed to {name}! The Queen stays in the other quarterfinal. Now the quarterfinal standings: only first place goes through.", "¡Cabeza de serie para {name}! La Reina permanece en el otro cuarto. Ahora, la tabla de los cuartos: solo pasa el primero."),
						Q("Uma vaga só. Nash segura o Plus no cais. O Muum joga Elemental devagar. A casa ataca como uma inscrição. Não tenha pressa na soma.", "One spot only. Nash holds Plus on the dock. Muum plays Elemental slowly. The house attacks as a single entry. Don't rush the sum.", "Una sola plaza. Nash aguanta el Plus en el muelle. Muum juega Elemental despacio. La casa ataca como una sola inscripción. No te apresures en la suma.")
					},
					new Line[] {
						N("Cabeça de chave sem vitória não se escreve. Revanche na mesma mesa. Ninguém sai do torneio por este resultado.", "A seed without a win doesn't get written down. Rematch at the same table. Nobody leaves the tournament on this result.", "Sin victoria no hay cabeza de serie. Revancha en la misma mesa. Nadie sale del torneo por este resultado.")
					,
						Q("Você esqueceu a parede. Morte súbita devolveu a casa.",
							"You forgot the wall. Sudden Death gave the square back.",
							"Olvidaste el muro. La Muerte súbita devolvió la casilla.")
					}),
				duel("pescador", "npc_pescador", "Nash Pell", "Nash Pell", "Nash Pell", 5, 6,
					Options.AIType.BALANCED, Ruleset.PLUS, 91, true,
					"Quartas de final", "Quarterfinal", "Cuartos de final",
					"Plus de cais. Calmo, e não solta a carta cedo.",
					"Dockside Plus. Calm, and he never lets go of a card early.",
					"Plus de muelle. Tranquilo, y no suelta la carta antes de tiempo.",
					new Line[] {
						N("Nash Pell pesca em FH e se inscreveu quando a fase chegou à plataforma.", "Nash Pell fishes at FH and signed up when the round came to the platform.", "Nash Pell pesca en FH y se inscribió cuando la fase llegó a la plataforma."),
						N("Compara carta com peixe sem transformar isso em guerra. Quer o primeiro lugar no grupo da casa.", "He compares a card to a fish without turning it into a war. He wants first place in the home group.", "Compara las cartas con peces sin convertirlo en una guerra. Quiere el primer puesto del grupo de casa."),
						N("O Leviathan é o prêmio fixo. Três pontos nas quartas.", "Leviathan is the fixed prize. Three points in the quarterfinal.", "Leviathan es el premio fijo. Tres puntos en los cuartos.")
					},
					new Line[] {
						N("Quartas de final em Fisherman's Horizon! Nash Pell contra {name}!", "Quarterfinal at Fisherman's Horizon! Nash Pell against {name}!", "¡Cuartos de final en Fisherman's Horizon! ¡Nash Pell contra {name}!"),
						N("Plus. Três pontos. Neste grupo, só o primeiro vai a Esthar. Leviathan fora da mão, na primeira vitória.", "Plus. Three points. In this group, only first place goes to Esthar. Leviathan, outside the hand, on the first win.", "Plus. Tres puntos. En este grupo, solo el primero va a Esthar. Leviathan, fuera de la mano, en la primera victoria.")
					},
					say("npc_pescador", "Nash Pell", "Nash Pell", "Nash Pell",
						"Linha curta. Plus de cais. Eu não solto a carta cedo. O Leviathan só sai do anzol se você fechar a soma que eu deixei aberta.",
						"Short line. Dockside Plus. I never let go of a card early. Leviathan only leaves the hook if you close the sum I left open.",
						"Línea corta. Plus de muelle. No suelto la carta pronto. Leviathan solo deja el anzuelo si cierras la suma que yo dejé abierta."),
					say("npc_pescador", "Nash Pell", "Nash Pell", "Nash Pell",
						"Fisgou. Você esperou o Plus no lance certo. Os Shumi jogam a seguir, Elemental, devagar. Não trate o Muum como se ele tivesse pressa.",
						"You hooked it. You waited for Plus on the right play. The Shumi play next, Elemental, slowly. Don't treat Muum as if he were in a hurry.",
						"Picó. Esperaste el Plus en la jugada correcta. Los Shumi juegan después, Elemental, despacio. No trates a Muum como si tuviera prisa."),
					lost("npc_pescador", "Nash Pell", "Nash Pell", "Nash Pell",
						"Escapou. Você puxou a linha cedo e eu recuei o Plus. Isca de novo. O grupo não espera no quase.",
						"It got away. You pulled the line early and I took Plus back. Bait again. The group doesn't wait on an almost.",
						"Se escapó. Tiraste de la línea pronto y yo retiré el Plus. Isca otra vez. El grupo no espera en el casi."),
					new Line[] {
						N("Três pontos nas quartas! O representante Shumi é o próximo.", "Three points in the quarterfinal! The Shumi representative is next.", "¡Tres puntos en los cuartos! El representante Shumi es el siguiente."),
						Q("O Muum joga devagar de propósito. Cada carta é uma casa. Elemental. Não confunda isso com fraqueza, e não antecipe o Combo.", "Muum plays slowly on purpose. Each card is a square. Elemental. Don't confuse that with weakness, and don't jump the Combo.", "Muum juega despacio a propósito. Cada carta es una casilla. Elemental. No lo confundas con debilidad, y no te adelantes al Combo.")
					},
					new Line[] {
						N("Nash segurou os pontos. Revanche. Nas quartas, só vitória conta, e só o primeiro lugar segue.", "Nash held on to the points. Rematch. In the quarterfinal only wins count, and only first place goes on.", "Nash retuvo los puntos. Revancha. En los cuartos solo cuenta la victoria, y solo sigue el primer puesto.")
					,
						Q("Você puxou cedo. O Plus voltou para o anzol dele.",
							"You pulled early. Plus went back on his hook.",
							"Tiraste pronto. El Plus volvió a su anzuelo.")
					}),
				duel("shumi", "shumi", "Ancião Muum", "Elder Muum", "Anciano Muum", 6, 7,
					Options.AIType.BALANCED, Ruleset.ELEMENT, 89, true,
					"Quartas de final", "Quarterfinal", "Cuartos de final",
					"Lento e Elemental. Cada carta é uma escultura.",
					"Slow and Elemental. Each card is a sculpture.",
					"Lento y Elemental. Cada carta es una escultura.",
					new Line[] {
						N("O ancião Muum veio da vila Shumi como representante. Uma inscrição, um nome.", "Elder Muum came from the Shumi Village as its representative. One entry, one name.", "El anciano Muum vino de la aldea Shumi como representante. Una inscripción, un nombre."),
						N("A vila pediu que alguém comparecesse. Ele ficou porque quer a forma certa e o primeiro lugar, nessa ordem.", "The village asked for someone to attend. He stayed because he wants the right shape and first place, in that order.", "La aldea pidió que alguien asistiera. Él se quedó porque quiere la forma correcta y el primer puesto, en ese orden."),
						N("O Carbuncle é o prêmio fixo desta mesa.", "Carbuncle is this table's fixed prize.", "Carbuncle es el premio fijo de esta mesa.")
					},
					new Line[] {
						N("Quartas, Fisherman's Horizon! Ancião Muum contra {name}!", "Quarterfinal, Fisherman's Horizon! Elder Muum against {name}!", "¡Cuartos, Fisherman's Horizon! ¡Anciano Muum contra {name}!"),
						N("Elemental, ritmo lento. Três pontos. Carbuncle na primeira vitória, separado da mão.", "Elemental, a slow pace. Three points. Carbuncle on the first win, apart from the hand.", "Elemental, ritmo lento. Tres puntos. Carbuncle en la primera victoria, separado de la mano.")
					},
					say("shumi", "Ancião Muum", "Elder Muum", "Anciano Muum",
						"A vila esculpe devagar. Eu jogo Elemental do mesmo jeito. O Carbuncle é minha forma mais limpa. Se você apressar a casa, eu fico com ela.",
						"The village sculpts slowly. I play Elemental the same way. Carbuncle is my cleanest form. If you rush the square, I keep it.",
						"La aldea esculpe despacio. Juego Elemental del mismo modo. Carbuncle es mi forma más limpia. Si apresuras la casilla, me la quedo."),
					say("shumi", "Ancião Muum", "Elder Muum", "Anciano Muum",
						"Boa forma. Você esperou o elemento e o Carbuncle muda de mãos. A Flo ainda representa a casa, no ataque, como uma inscrição só.",
						"Good form. You waited on the element and Carbuncle changes hands. Flo still represents the house, on the attack, as a single entry.",
						"Buena forma. Esperaste el elemento y Carbuncle cambia de manos. Flo aún representa a la casa, al ataque, como una sola inscripción."),
					lost("shumi", "Ancião Muum", "Elder Muum", "Anciano Muum",
						"Ainda está cru. Você soltou a carta no meio da forma. Trabalhe de novo. Eu espero na mesma casa.",
						"Still rough. You dropped the card in the middle of the shape. Work it again. I'll wait on the same square.",
						"Aún está crudo. Soltaste la carta a mitad de la forma. Trabaja otra vez. Yo espero en la misma casilla."),
					new Line[] {
						N("{name} supera o Muum na tabela! O último confronto das quartas é contra Dobe e Flo, numa inscrição só.", "{name} passes Muum in the standings! The last quarterfinal match is Dobe and Flo, a single entry.", "¡{name} supera a Muum en la tabla! El último duelo de los cuartos es contra Dobe y Flo, en una sola inscripción."),
						Q("Eles jogam como a casa, não como dois contra um. Uma inscrição, Elemental no ataque. Phoenix está em casa. Respeite a parede deles.", "They play as the house, not two against one. One entry, Elemental on the attack. Phoenix is at home. Respect their wall.", "Juegan como la casa, no como dos contra uno. Una inscripción, Elemental al ataque. Phoenix está en casa. Respeta su muro.")
					},
					new Line[] {
						N("O Muum não cedeu os pontos. Revanche, no tempo dele. A vaga na semifinal exige o primeiro lugar.", "Muum didn't give up the points. Rematch, at his pace. The semifinal spot takes first place, outright.", "Muum no cedió los puntos. Revancha, a su ritmo. La plaza en la semifinal exige el primer puesto.")
					,
						Q("Você soltou no meio da forma. Ele espera a casa pronta.",
							"You dropped it in the middle of the shape. He waits for the square to be ready.",
							"Soltaste a mitad de la forma. Él espera a que la casilla esté lista.")
					}),
				duel("flo", "dobe_flo", "Dobe e Flo", "Dobe and Flo", "Dobe y Flo", 6, 8,
					Options.AIType.OFFENSIVE, Ruleset.ELEMENT, 96, true,
					"Quartas de final", "Quarterfinal", "Cuartos de final",
					"A casa joga no ataque, como uma inscrição só.",
					"The house plays on the attack, as a single entry.",
					"La casa juega al ataque, como una sola inscripción.",
					new Line[] {
						N("Dobe, o prefeito, e Flo entram como uma única inscrição de FH. A comunidade se senta junta; a mão é uma só.", "Dobe, the mayor, and Flo enter as a single FH entry. The community sits together; the hand is one.", "Dobe, el alcalde, y Flo entran como una única inscripción de FH. La comunidad se sienta junta; la mano es una sola."),
						N("Querem que as quartas tenham a cara da plataforma, e querem o primeiro lugar. Não dão a vaga de presente.", "They want the quarters to feel like the platform, and they want first place. They don't give the spot away.", "Quieren que los cuartos tengan el aire de la plataforma, y quieren el primer puesto. No regalan la plaza."),
						N("A Phoenix é o prêmio fixo. Vencer aqui, somado ao resto da tabela, manda {name} à semifinal.", "Phoenix is the fixed prize. A win here, with the rest of the standings, sends {name} to the semifinal.", "Phoenix es el premio fijo. Ganar aquí, sumado al resto de la tabla, manda a {name} a la semifinal.")
					},
					new Line[] {
						N("Fechamento do grupo das quartas! Dobe e Flo contra {name}!", "The quarterfinal group's last match! Dobe and Flo against {name}!", "¡Cierre del grupo de los cuartos! ¡Dobe y Flo contra {name}!"),
						N("Elemental ofensivo. Três pontos. Só o primeiro deste grupo vai a Esthar. Phoenix na primeira vitória.", "Offensive Elemental. Three points. Only first place in this group goes to Esthar. Phoenix on the first win.", "Elemental ofensivo. Tres puntos. Solo el primero de este grupo va a Esthar. Phoenix en la primera victoria.")
					},
					say("dobe_flo", "Flo", "Flo", "Flo",
						"O prefeito e eu jogamos como um só. Elemental, no ataque. A Phoenix está em casa. Aguenta o Plus de FH, {name}.",
						"The mayor and I play as one. Elemental, on the attack. Phoenix is at home. Hold up to FH's Plus, {name}.",
						"El alcalde y yo jugamos como uno solo. Elemental, al ataque. Phoenix está en casa. Aguanta el Plus de FH, {name}."),
					say("dobe_flo", "Flo", "Flo", "Flo",
						"A plataforma viu o Combo fechar. A Phoenix é sua. Siga se a tabela deixar. A casa aplaude mesmo assim.",
						"The platform saw the Combo close. Phoenix is yours. Go on if the table allows it. The house applauds either way.",
						"La plataforma vio cerrarse el Combo. Phoenix es tuya. Sigue si la tabla lo permite. La casa aplaude igual."),
					lost("dobe_flo", "Flo", "Flo", "Flo",
						"A casa ganhou esta. Você abriu o ataque e não segurou o Combo. Beba uma água e volte. A vaga não sai no quase.",
						"The house won this one. You opened the attack and didn't hold the Combo. Drink some water and come back. The spot doesn't leave on an almost.",
						"La casa ganó esta. Abriste el ataque y no aguantaste el Combo. Bebe agua y vuelve. La plaza no se va en el casi."),
					new Line[] {
						N("Quartas encerradas para {name}! O primeiro lugar vai à semifinal em Esthar. O outro grupo eu conto lá, sem inventar tabela.", "The quarterfinal is closed for {name}! First place goes to the semifinal in Esthar. I'll report on the other group there, without making up standings.", "¡Cuartos cerrados para {name}! El primer puesto va a la semifinal en Esthar. El otro grupo lo cuento allá, sin inventar tablas."),
						Q("Esthar é luz fria. Lá entram Same Wall e Morte súbita. O Seifer chegou por aquele lado. Eu vi o nome. Não provoque a parede dele.", "Esthar is cold light. Same Wall and Sudden Death come in there. Seifer arrived from that side. I saw the name. Don't bait his wall.", "Esthar es luz fría. Allí entran Same Wall y Muerte súbita. Seifer llegó por ese lado. Vi el nombre. No provoques su muro.")
					},
					new Line[] {
						N("A casa segurou os três pontos. Revanche contra Dobe e Flo. Sem isso, a semifinal não terá {name}.", "The house held the three points. Rematch against Dobe and Flo. Without them, {name} won't be in the semifinal.", "La casa retuvo los tres puntos. Revancha contra Dobe y Flo. Sin eso, {name} no estará en la semifinal.")
					,
						Q("O Combo da casa não parou. O seu parou.",
							"The house Combo didn't stop. Yours did.",
							"El Combo de la casa no se detuvo. El tuyo sí.")
					})
			});
	}

	private static Region esthar() {
		return region("esthar", "chegada_esthar", "mesa_esthar.png", Kind.GROUP, 1,
			new String[][] {{"seifer", "cientista"}, {"seifer", "odine"}, {"odine", "cientista"}},
			"Esthar - Semifinal", "Esthar - Semifinal", "Esthar - Semifinal",
			new Line[] {
				N("{name} terminou em primeiro nas quartas de FH. A semifinal deste lado é em Esthar.", "{name} finished first in the FH quarterfinal. This side's semifinal is in Esthar.", "{name} terminó {primero|primera} en los cuartos de FH. La semifinal de este lado es en Esthar."),
				N("A cidade é a sede porque o laboratório tem sala, luz e público. Não porque haja uma crise. O assunto é o torneio.", "The city hosts it because the lab has a room, lights and a crowd. Not because there's a crisis. The tournament is the story.", "La ciudad es la sede porque el laboratorio tiene sala, luz y público. No porque haya una crisis. El tema es el torneo."),
				Q("O Seifer subiu pela chave dele desde o Garden. Está neste grupo, com todas as regras. A Rainha joga o outro. Não vou fingir que tenho a tabela de lá.", "Seifer climbed his own ladder from the Garden. He's in this group, with every rule on. The Queen plays the other one. I won't pretend I have that table.", "Seifer subió por su llave desde el Garden. Está en este grupo, con todas las reglas. La Reina juega el otro. No voy a fingir que tengo esa tabla."),
				N("Grupo da semifinal: {name}, Ivo Lenn, Odine e Seifer! Vitória vale 3. Só o primeiro vai à grande final.", "Semifinal group: {name}, Ivo Lenn, Odine and Seifer! A win is worth 3. Only first place goes to the grand final.", "Grupo de la semifinal: ¡{name}, Ivo Lenn, Odine y Seifer! La victoria vale 3. Solo el primero va a la gran final."),
				say("seifer", "Seifer", "Seifer", "Seifer",
					"Olha só quem sobreviveu às tabelas. {name}, eu não vim aplaudir. Vim ser a última mesa: Same Wall, Morte súbita, a minha carta no fundo da mão.",
					"Look who survived the tables. {name}, I didn't come to clap. I came to be the last table: Same Wall, Sudden Death, my card at the bottom of the hand.",
					"Mira quién sobrevivió a las tablas. {name}, no vine a aplaudir. Vine a ser la última mesa: Same Wall, Muerte súbita, mi carta al fondo de la mano.")
			},
			new Duel[] {
				duel("cientista", "npc_cientista_esthar", "Ivo Lenn", "Ivo Lenn", "Ivo Lenn", 6, 7,
					Options.AIType.OFFENSIVE, Ruleset.ELEMENT, 92, true,
					"Semifinal", "Semifinal", "Semifinal",
					"Ofensivo, de laboratório. Elemental calculado.",
					"Offensive, lab-made. Calculated Elemental.",
					"Ofensivo, de laboratorio. Elemental calculado.",
					new Line[] {
						N("Ivo Lenn é assistente de Odine. Entrou porque o laboratório pediu um jogador. Ficou porque quer a vitória no próprio nome.", "Ivo Lenn is Odine's assistant. He entered because the lab asked for a player. He stayed because he wants the win in his own name.", "Ivo Lenn es asistente de Odine. Entró porque el laboratorio pidió un jugador. Se quedó porque quiere la victoria en su propio nombre."),
						N("Não fala como o chefe. Anota, joga para a frente e não pede licença na mesa.", "He doesn't talk like his boss. He takes notes, plays forward, and doesn't ask permission at the table.", "No habla como el jefe. Anota, juega hacia adelante y no pide permiso en la mesa."),
						N("O Odin é o prêmio fixo. Três pontos na semifinal.", "Odin is the fixed prize. Three points in the semifinal.", "Odin es el premio fijo. Tres puntos en la semifinal.")
					},
					new Line[] {
						N("Esthar, grupo da semifinal! Ivo Lenn contra {name}!", "Esthar, semifinal group! Ivo Lenn against {name}!", "¡Esthar, grupo de la semifinal! ¡Ivo Lenn contra {name}!"),
						N("Elemental ofensivo. Três pontos. Só o primeiro deste grupo vai à final no Garden. Odin fora da mão.", "Offensive Elemental. Three points. Only first place in this group goes to the final at the Garden. Odin, outside the hand.", "Elemental ofensivo. Tres puntos. Solo el primero de este grupo va a la final en el Garden. Odin, fuera de la mano.")
					},
					say("npc_cientista_esthar", "Ivo Lenn", "Ivo Lenn", "Ivo Lenn",
						"O Odine pediu um teste. Eu pedi a cadeira. Elemental calculado, no ataque. Se você virar o Odin, ele sai com você. Se perder, repete a soma.",
						"Odine asked for a test. I asked for the chair. Calculated Elemental, on the attack. If you flip Odin, he leaves with you. If you lose, you repeat the sum.",
						"Odine pidió una prueba. Yo pedí la silla. Elemental calculado, al ataque. Si volteas a Odin, se va contigo. Si pierdes, repites la suma."),
					say("npc_cientista_esthar", "Ivo Lenn", "Ivo Lenn", "Ivo Lenn",
						"Registrado no meu nome, não no dele. Você leu o elemento no lance que o laboratório não previu. O Odine já quer o próximo, com todas as regras. Boa sorte com isso.",
						"Logged under my name, not his. You read the element on the play the lab didn't predict. Odine already wants the next one, with every rule on. Good luck with that.",
						"Registrado a mi nombre, no al suyo. Leíste el elemento en la jugada que el laboratorio no previó. Odine ya quiere la siguiente, con todas las reglas. Suerte con eso."),
					lost("npc_cientista_esthar", "Ivo Lenn", "Ivo Lenn", "Ivo Lenn",
						"Não fechou. Você pulou a casa elemental e eu completei o ataque. A gente recomeça. Eu não empresto pontos.",
						"It didn't close. You skipped the elemental square and I finished the attack. We start over. I don't lend points.",
						"No cerró. Te saltaste la casilla elemental y yo terminé el ataque. Empezamos de nuevo. Yo no presto puntos."),
					new Line[] {
						N("Três pontos na semifinal! O próximo é Odine, e ele joga por conta própria.", "Three points in the semifinal! Odine is next, and he plays for himself.", "¡Tres puntos en la semifinal! El siguiente es Odine, y juega por su cuenta."),
						Q("Não discuta teoria com o Odine. Discuta a mesa. Todas as regras, ego grande, jogo longo. Eden não sai numa Morte súbita mal lida.", "Don't argue theory with Odine. Argue the table. Every rule, a big ego, a long game. Eden doesn't leave on a Sudden Death you misread.", "No discutas teoría con Odine. Discute la mesa. Todas las reglas, orgullo grande, partida larga. Eden no se va en una Muerte súbita mal leída.")
					},
					new Line[] {
						N("Lenn segurou os pontos. Revanche. A semifinal não classifica ninguém no quase.", "Lenn held on to the points. Rematch. The semifinal doesn't qualify anyone on almost.", "Lenn retuvo los puntos. Revancha. La semifinal no clasifica a nadie por un casi.")
					,
						Q("Você pulou a casa elemental. O laboratório não pula.",
							"You skipped the elemental square. The lab doesn't skip.",
							"Te saltaste la casilla elemental. El laboratorio no se salta.")
					}),
				duel("odine", "odine", "Odine", "Odine", "Odine", 7, 7,
					Options.AIType.OFFENSIVE, Ruleset.FINAL, 99, true,
					"Semifinal", "Semifinal", "Semifinal",
					"Todas as regras. Orgulho grande, jogo longo.",
					"Every rule. Big ego, long game.",
					"Todas las reglas. Orgullo grande, partida larga.",
					new Line[] {
						N("Odine entrou pelo título e pelos dados; quando a mesa aperta, nessa ordem. Não repete a fala do assistente.", "Odine entered for the title and the data; when the table gets tight, in that order. He doesn't repeat his assistant's lines.", "Odine entró por el título y por los datos; cuando la mesa aprieta, en ese orden. No repite el discurso de su asistente."),
						N("Esthar é a casa dele. Isso não lhe dá pontos antes da carta.", "Esthar is his home. That doesn't earn him points before the card is down.", "Esthar es su casa. Eso no le da puntos antes de la carta."),
						N("O Eden é o prêmio fixo. Só na primeira vitória, nunca na escolha comum.", "Eden is the fixed prize. First win only, never in the regular pick.", "Eden es el premio fijo. Solo en la primera victoria, nunca en la elección común.")
					},
					new Line[] {
						N("Semifinal, Esthar! Odine contra {name}!", "Semifinal, Esthar! Odine against {name}!", "¡Semifinal, Esthar! ¡Odine contra {name}!"),
						N("Regras completas, com Morte súbita se empatar. Três pontos. Eden separado da mão.", "Full rules, with Sudden Death on a draw. Three points. Eden, apart from the hand.", "Reglas completas, con Muerte súbita si hay empate. Tres puntos. Eden, aparte de la mano.")
					},
					say("odine", "Odine", "Odine", "Odine",
						"O Eden ainda é teoria até alguém me vencer. Todas as regras: Same Wall, Morte súbita. Os dados podem esperar. Os pontos, não.",
						"Eden is still a theory until someone beats me. Every rule: Same Wall, Sudden Death. The data can wait. The points cannot.",
						"Eden sigue siendo teoría hasta que alguien me gane. Todas las reglas: Same Wall, Muerte súbita. Los datos pueden esperar. Los puntos, no."),
					say("odine", "Odine", "Odine", "Odine",
						"Confirmado. Irritante. Você usou a parede melhor do que os meus números. O Seifer é o último deste grupo. Não me peça para explicá-lo. Jogue a Morte súbita até o fim.",
						"Confirmed. Irritating. You used the wall better than my numbers. Seifer is the last of this group. Don't ask me to explain him. Play Sudden Death to the end.",
						"Confirmado. Irritante. Usaste el muro mejor que mis números. Seifer es el último de este grupo. No me pidas que lo explique. Juega la Muerte súbita hasta el final."),
					lost("odine", "Odine", "Odine", "Odine",
						"Não serve. Você esqueceu Same Wall no último lance e a Morte súbita me devolveu o Eden. Volte com uma mão melhor. Eu continuo aqui.",
						"Not good enough. You forgot Same Wall on the last play and Sudden Death gave me Eden back. Come back with a better hand. I'll still be here.",
						"No sirve. Olvidaste Same Wall en la última jugada y la Muerte súbita me devolvió a Eden. Vuelve con una mano mejor. Yo sigo aquí."),
					new Line[] {
						N("{name} tira pontos de Odine! A última mesa da semifinal é contra o Seifer.", "{name} takes points off Odine! The semifinal's last table is Seifer.", "¡{name} le quita puntos a Odine! La última mesa de la semifinal es contra Seifer."),
						Q("Ele provoca desde o Garden. Deixa. Same Wall e Morte súbita respondem melhor que você. A carta dele só sai se a parede cair.", "He's been needling you since the Garden. Leave it. Same Wall and Sudden Death answer better than you do. His card only leaves if the wall falls.", "Provocó desde el Garden. Déjalo. Same Wall y Muerte súbita responden mejor que tú. Su carta solo se va si cae el muro.")
					},
					new Line[] {
						N("Odine não cedeu. Revanche com as mesmas regras. Sem este jogo, o primeiro lugar não se completa.", "Odine didn't give in. Rematch under the same rules. First place isn't settled without this match.", "Odine no cedió. Revancha con las mismas reglas. Sin este partido, el primer puesto no se completa.")
					,
						Q("Same Wall no último lance. A Morte súbita ficou com ele.",
							"Same Wall on the last play. Sudden Death stayed with him.",
							"Same Wall en la última jugada. La Muerte súbita se quedó con él.")
					}),
				duel("seifer", "seifer", "Seifer", "Seifer", "Seifer", 6, 7,
					Options.AIType.OFFENSIVE, Ruleset.FINAL, 109, true,
					"Semifinal", "Semifinal", "Semifinal",
					"Ofensivo e provocador. Todas as regras.",
					"Offensive and needling. Every rule on.",
					"Ofensivo y provocador. Todas las reglas.",
					new Line[] {
						N("Seifer Almasy, aluno de Balamb, rival de {name} desde a inscrição. Subiu pela outra chave e caiu neste grupo.", "Seifer Almasy, a Balamb student, {name}'s rival since registration. He climbed the other side of the bracket and landed in this group.", "Seifer Almasy, alumno de Balamb, rival de {name} desde la inscripción. Subió por la otra llave y cayó en este grupo."),
						N("Quer o título para dizer que a cadeira do meio é dele. Não está aqui a mando de ninguém.", "He wants the title so he can say the middle chair is his. He isn't here on anyone's orders.", "Quiere el título para decir que la silla del medio es suya. No está aquí por orden de nadie."),
						N("A carta Seifer é o prêmio fixo. Não aparece na mão. Vencer este grupo manda {name} à final.", "The Seifer card is the fixed prize. It doesn't appear in the hand. Winning this group sends {name} to the final.", "La carta de Seifer es el premio fijo. No aparece en la mano. Ganar este grupo manda a {name} a la final.")
					},
					new Line[] {
						N("Esthar! Última mesa da semifinal! Seifer contra {name}!", "Esthar! The semifinal's last table! Seifer against {name}!", "¡Esthar! ¡Última mesa de la semifinal! ¡Seifer contra {name}!"),
						N("Regras completas. Três pontos. Se a tabela fechar com {name} em primeiro, a grande final é no Garden. A carta dele só na primeira vitória.", "Full rules. Three points. If the standings close with {name} in first, the grand final is at the Garden. His card only on the first win.", "Reglas completas. Tres puntos. Si la tabla cierra con {name} {primero|primera}, la gran final es en el Garden. Su carta solo en la primera victoria.")
					},
					say("seifer", "Seifer", "Seifer", "Seifer",
						"{name}. {Sonhador|Sonhadora} até aqui. Última mesa do grupo: Same Wall, Morte súbita, todas as regras. Minha carta só sai se a parede cair comigo.",
						"{name}. Still dreaming this far. Last table of the group: Same Wall, Sudden Death, every rule on. My card only leaves if the wall falls with me.",
						"{name}. {Soñador|Soñadora} hasta aquí. Última mesa del grupo: Same Wall, Muerte súbita, todas las reglas. Mi carta solo se va si el muro cae conmigo."),
					say("seifer", "Seifer", "Seifer", "Seifer",
						"...Leva. Você segurou a Morte súbita no lance que eu quis apressar. Não se acostuma. Se a tabela te puser na final, eu vou estar na arquibancada. Calado, não.",
						"...Take it. You held Sudden Death on the play I wanted to rush. Don't get used to it. If the table puts you in the final, I'll be in the stands. Quiet? No.",
						"...Llévatela. Aguantaste la Muerte súbita en la jugada que yo quise acelerar. No te acostumbres. Si la tabla te pone en la final, estaré en la grada. ¿Callado? No."),
					lost("seifer", "Seifer", "Seifer", "Seifer",
						"Ainda sonhando? Você deixou a parede aberta e eu fechei o Same. Levanta. A semifinal não tem versão fácil, e Morte súbita não espera.",
						"Still dreaming? You left the wall open and I closed the Same. Get up. The semifinal has no easy version, and Sudden Death doesn't wait.",
						"¿Sigues soñando? Dejaste el muro abierto y yo cerré el Same. Levántate. La semifinal no tiene versión fácil, y la Muerte súbita no espera."),
					new Line[] {
						N("Semifinal deste lado encerrada! Se {name} for {o primeiro|a primeira}, a grande final é em Balamb Garden.", "This side's semifinal is closed! If {name} finished first, the grand final is at Balamb Garden.", "¡Semifinal de este lado cerrada! Si {name} es {el primero|la primera}, la gran final es en Balamb Garden."),
						N("A Rainha venceu o outro grupo. Isso eu vi na súmula, e só digo o resultado: a final é contra ela. A tabela de lá não está nesta sala.", "The Queen won the other group. I saw that in the record, and I'll only say the result: the final is against her. That table isn't in this room.", "La Reina ganó el otro grupo. Eso lo vi en el acta, y solo digo el resultado: la final es contra ella. La tabla de allá no está en esta sala."),
						Q("De volta ao Garden. Desta vez o salão é seu. Todas as regras, uma mesa, o título. Eu fico atrás da cadeira. Você joga a parede.", "Back to the Garden. This time the hall is yours. Every rule, one table, the title. I'll stay behind the chair. You play the wall.", "De vuelta al Garden. Esta vez el salón es tuyo. Todas las reglas, una mesa, el título. Yo me quedo detrás de la silla. Tú juegas el muro.")
					},
					new Line[] {
						N("Seifer segurou a semifinal nesta mesa. Revanche. O primeiro lugar, e só ele, vai à final.", "Seifer held the semifinal at this table. Rematch. First place, and only first place, goes to the final.", "Seifer retuvo la semifinal en esta mesa. Revancha. El primer puesto, y solo él, va a la final.")
					,
						Q("Você deixou a parede aberta. Ele não deixa.",
							"You left the wall open. He doesn't.",
							"Dejaste el muro abierto. Él no lo deja.")
					})
			});
	}

	private static Region fin() {
		return region("final", "final_historia", StoryCampaign.FINAL_ARENA, Kind.FINAL, 0, null,
			"Garden - Grande final", "Garden - Grand Final", "Garden - Gran final",
			new Line[] {
				Q("{name}. Você voltou {campeão|campeã} de fase pelos resultados, não pelo discurso. A final é uma mesa só: Same Wall, Morte súbita, o Bahamut fora da mão dela.", "{name}. You came back a stage champion on results, not speeches. The final is a single table: Same Wall, Sudden Death, Bahamut kept out of her hand.", "{name}. Volviste {campeón|campeona} de fase por resultados, no por discursos. La final es una sola mesa: Same Wall, Muerte súbita, Bahamut fuera de su mano."),
				N("O Garden sedia a grande final porque a inscrição começou neste corredor. O círculo se fecha aqui!", "The Garden hosts the grand final because registration started in this hall. The circle closes here!", "El Garden acoge la gran final porque la inscripción empezó en este pasillo. ¡El círculo se cierra aquí!"),
				N("Uma partida. Derrota não entrega o título: a revanche é na mesma final, na hora. O título só muda de mãos com a sua vitória.", "One match. A loss doesn't hand over the title: the rematch is the same final, on the spot. The title only changes hands with your win.", "Una partida. La derrota no entrega el título: la revancha es en la misma final, al momento. El título solo cambia de manos con tu victoria."),
				N("Do outro grupo, a súmula diz que a Rainha venceu. Não vou fingir uma chave que não está nesta sala. A adversária, essa sim, está.", "From the other group, the record says the Queen won. I won't fake a bracket that isn't in this room. The opponent, though, is right here.", "Del otro grupo, el acta dice que la Reina ganó. No voy a fingir un cuadro que no está en esta sala. La rival, esa sí está."),
				say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
					"De Balamb ao cais, e de volta. {name}, agora não é grupo. É o título. Todas as regras. O Bahamut não está na minha mão. A parede, sim.",
					"From Balamb to the dock, and back. {name}, this isn't a group. It's the title. Every rule. Bahamut is not in my hand. The wall is.",
					"De Balamb al muelle, y de vuelta. {name}, ahora no es un grupo. Es el título. Todas las reglas. Bahamut no está en mi mano. El muro, sí."),
				Q("Eu fico atrás da cadeira. O Nilo anuncia. Você joga a parede. ...Whatever. Não me faça repetir.", "I'll stay behind the chair. Nilo calls it. You play the wall. ...Whatever. Don't make me repeat it.", "Yo me quedo detrás de la silla. Nilo anuncia. Tú juegas el muro. ...Whatever. No me hagas repetirlo.")
			},
			new Duel[] {
				duel("queen_esthar", "card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas", 6, 7,
					Options.AIType.OFFENSIVE, Ruleset.FINAL, 97, false,
					"Grande final", "Grand final", "Gran final",
					"Todas as regras. A mão dela não inclui o Bahamut.",
					"Every rule. Her hand doesn't include Bahamut.",
					"Todas las reglas. Su mano no incluye a Bahamut.",
					new Line[] {
						N("A Rainha das Cartas passou pelo porto, por Deling, por Dollet e pela partida de cabeça de chave em FH. Nenhum desses jogos era a final.", "The Queen of Cards made it through the harbor, Deling, Dollet and the seeding match at FH. None of those games was the final.", "La Reina de las Cartas pasó por el puerto, Deling, Dollet y la partida de cabeza de serie en FH. Ninguno de esos partidos era la final."),
						N("Ela quer o título de volta, em público, no Garden. Trata {name} pelo nome, e pelo que a tabela já mostrou.", "She wants the title back, in public, at the Garden. She calls {name} by name now, and by what the standings have shown.", "Ella quiere el título de vuelta, en público, en el Garden. Trata a {name} por el nombre, y por lo que la tabla ya mostró."),
						N("O Bahamut é o prêmio fixo da primeira vitória. Não está na mão. A carta escolhida vem além dele, uma de cada.", "Bahamut is the fixed prize for the first win. It isn't in the hand. The chosen card comes on top of it, one of each.", "Bahamut es el premio fijo de la primera victoria. No está en la mano. La carta elegida viene además de él, una de cada.")
					},
					new Line[] {
						N("Balamb Garden! Grande final do Torneio Mundial! A Rainha das Cartas contra {name}!", "Balamb Garden! The grand final of the World Tournament! The Queen of Cards against {name}!", "¡Balamb Garden! ¡Gran final del Torneo Mundial! ¡La Reina de las Cartas contra {name}!"),
						N("Same, Same Wall, Plus, Combo, Elemental e Morte súbita. Em jogo: o título. Bahamut só se {name} vencer pela primeira vez.", "Same, Same Wall, Plus, Combo, Elemental and Sudden Death. At stake: the title. Bahamut only if {name} wins for the first time.", "Same, Same Wall, Plus, Combo, Elemental y Muerte súbita. En juego: el título. Bahamut solo si {name} gana por primera vez.")
					},
					say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"Sem grupo, sem cabeça de chave. {name}, Same Wall e Morte súbita. Se a mesa for sua, o Bahamut e o título saem juntos. Eu não entrego os dois sorrindo.",
						"No group, no seed match. {name}, Same Wall and Sudden Death. If the table is yours, Bahamut and the title leave together. I don't hand both over with a smile.",
						"Sin grupo, sin cabeza de serie. {name}, Same Wall y Muerte súbita. Si la mesa es tuya, Bahamut y el título se van juntos. No entrego los dos sonriendo."),
					say("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"Você segurou a parede no último Same e a Morte súbita não me devolveu nada. Leva o Bahamut. O título, eu digo em voz alta: hoje é de {name}.",
						"You held the wall on the last Same, and Sudden Death gave me nothing back. Take Bahamut. The title, I'll say out loud: today it belongs to {name}.",
						"Aguantaste el muro en el último Same, y la Muerte súbita no me devolvió nada. Llévate a Bahamut. El título lo digo en voz alta: hoy es de {name}."),
					lost("card_queen", "Rainha das Cartas", "Queen of Cards", "Reina de las Cartas",
						"O título fica. Você esqueceu Same Wall e eu recuperei a casa na Morte súbita. A final não tem prêmio de consolação. Senta de novo.",
						"The title stays. You forgot Same Wall and I took the square back on Sudden Death. The final has no consolation prize. Sit down again.",
						"El título se queda. Olvidaste Same Wall y recuperé la casilla en la Muerte súbita. La final no tiene premio de consolación. Siéntate otra vez."),
					new Line[] {
						N("A mesa acabou. Antes da cerimônia, {name} escolhe uma carta da mão. O Bahamut, se for a primeira vez, vem junto, à parte.", "The table is done. Before the ceremony, {name} picks one card from the hand. Bahamut, if it's the first time, comes along separately.", "La mesa terminó. Antes de la ceremonia, {name} elige una carta de la mano. Bahamut, si es la primera vez, viene también, aparte."),
						Q("Escolha a carta com calma. A parede já fez a parte dela. O salão espera.", "Pick the card slowly. The wall already did its job. The hall is waiting.", "Elige la carta con calma. El muro ya hizo su parte. El salón espera.")
					},
					new Line[] {
						N("A grande final continua aberta. A regra é revanche na mesma mesa. O título não muda de mãos numa derrota.", "The grand final stays open. The rule is a rematch at the same table. The title doesn't change hands on a loss.", "La gran final sigue abierta. La regla es revancha en la misma mesa. El título no cambia de manos en una derrota.")
					,
						Q("Same Wall. Morte súbita. Você esqueceu um dos dois. O título ficou.",
							"Same Wall. Sudden Death. You forgot one of the two. The title stayed.",
							"Same Wall. Muerte súbita. Olvidaste uno de los dos. El título se quedó.")
					})
			});
	}

	private static Line N(String pt, String en, String es) {
		return new Line(true, StoryCampaign.NARRATOR_PORTRAIT, false, "Nilo Vann", "Nilo Vann", "Nilo Vann", pt, en, es);
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
