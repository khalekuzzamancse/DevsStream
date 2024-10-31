import 'package:ComposableWidget/composable_widget.dart';
import 'package:flutter/material.dart';

class HomeController {
  final user = 'George';
  final  cards =[
    CardInfo(
        cardName: 'VISA',
        cardNo: '* * * 3854',
        dueDate: '10 OCT',
        amount: '\$5001.86',
        color: Colors.black
    ),
    CardInfo(
        cardName: 'VISA',
        cardNo: '* * * 3854',
        dueDate: '10 OCT',
        amount: '\$5001.86',
        color: Colors.blue
    ),
  ];

  // List of bill payment items
  final List<BillPayment> billPayments = [
    BillPayment(title: 'Electricity Bill', icon: Icons.lightbulb_outline),
    BillPayment(title: 'Internet Recharge', icon: Icons.wifi_outlined),
    BillPayment(title: 'Cable Bill', icon: Icons.tv_outlined),
    BillPayment(title: 'Mobile Recharge', icon: Icons.smartphone_outlined),
  ];

  final activeLoanItems=[
    LoanItemData(
      model: "Model X",
      imageLink: "https://img.freepik.com/premium-vector/red-city-car-vector-illustration_648968-44.jpg?w=740",
      price: "\$399/M",
      date: "5th OCT",
      rating: 48,
      ratingMax: 60,
    ),
    LoanItemData(
      model: "Nokia Y",
      imageLink: "https://auspost.com.au/shop/static/WFS/AusPost-Shop-Site/-/AusPost-Shop-auspost-B2CWebShop/en_AU/feat-cat/mobile-phones/category-carousel/MP_UnlockedPhones_3.jpg",
      price: "\$299/M",
      date: "20 OCT",
      rating: 36,
      ratingMax: 50,
    ),
  ];

}

//@formatter:off
class HomeScreen extends StatelessWidget {
  final HomeController controller = HomeController();

  HomeScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          '✌️ Hey ${controller.user}!',
          style: TextStyle(fontWeight: FontWeight.w500, fontSize: 20),
        ),
        actions: [
          IconButton(
            icon: Icon(Icons.search),
            onPressed: () {},
          ),
          IconButton(
            icon: Padding(
              padding: const EdgeInsets.all(8.0),
              child: Icon(Icons.notifications_none_outlined),
            ),
            onPressed: () {},
          ),
        ],
      ),
      body: SingleChildScrollView(
        child: Padding(
          padding: const EdgeInsets.all(12.0),
          child: Column(
            children: [
              _Cards(cards: controller.cards),
              SizedBox(height: 16), // Replacing VSpacer with SizedBox
              _PayBillSection(
                items: controller.billPayments,
              ),
              SizedBox(height: 16),
              _ActiveLoanSection(
                loanItems: controller.activeLoanItems,
              )

            ],
          ),
        ),
      ),
    );
  }
}


class CardInfo {
  final String cardName;
  final String cardNo;
  final String dueDate;
  final String amount;
  final Color color;//TODO:Controller belongs to `Presentation` Logic layer that is UI framework independent
  //so should not keep the color reference at controller, instead card type or metadata should be kept based on
  //these data ui will determine the color,but for testing purpose storing color here
  //How ever since color code is just text, so color code can be kept...


  CardInfo( {
    required this.color,
    required this.cardName,
    required this.cardNo,
    required this.dueDate,
    required this.amount,
  });
}

class _Cards extends StatelessWidget {
  final List<CardInfo> cards;
  const _Cards({super.key, required this.cards});

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        SingleChildScrollView(
          scrollDirection: Axis.horizontal,
          physics: AlwaysScrollableScrollPhysics(),
          child: Row(
            children: cards
                .map((item) => Padding(
              padding: const EdgeInsets.all(8.0),
              child: _Card(cardInfo: item),
            ))
                .toList(),
          ),
        ),
        SizedBox(height: 8), // Spacer between cards and indicators
        // Indicators
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: List.generate(
            cards.length,
                (index) => Padding(
              padding: const EdgeInsets.symmetric(horizontal: 4.0),
              child: CircleAvatar(
                radius: 3,
                backgroundColor: Colors.black,
              ),
            ),
          ),
        ),
      ],
    );
  }
}



class _Card extends StatelessWidget {
  final CardInfo cardInfo;
  Color textColor = Colors.black;
  Color buttonTextColor = Colors.black;
  final Color bgColor;
  final Color buttonColor;

  _Card({
    super.key,
    required this.cardInfo,
    this.bgColor = Colors.black,
    this.buttonColor = Colors.white,
  }) {
    textColor = cardInfo.color.computeLuminance() > 0.5 ? Colors.black : Colors.white;
    buttonTextColor = buttonColor.computeLuminance() > 0.5 ? Colors.black : Colors.white;
  }

  @override
  Widget build(BuildContext context) {
    final labelSmall = TextStyle(fontSize: 13, fontWeight: FontWeight.w400, color: textColor);
    final labelMedium = TextStyle(fontSize: 18, color: textColor, fontWeight: FontWeight.w500);

    return Container(
      constraints: BoxConstraints(maxWidth: 300),
      decoration: BoxDecoration(
        color: cardInfo.color,
        borderRadius: BorderRadius.circular(12),
        boxShadow: [
          BoxShadow(color: Colors.black.withOpacity(0.1), spreadRadius: 1, blurRadius: 5, offset: Offset(0, 4))
        ],
      ),
      padding: const EdgeInsets.all(16),
      child: _CardLayoutStrategy(
        cardName: Text(cardInfo.cardName, style: labelMedium.copyWith(fontStyle: FontStyle.italic)),
        cardNo: Text(cardInfo.cardNo, style: labelMedium),
        dueDate: Row(
          children: [
            Text('Due date', style: labelSmall.copyWith(fontWeight: FontWeight.w300)),
            SizedBox(width: 8),
            Text(cardInfo.dueDate, style: labelSmall),
          ],
        ),
        labelEarly: Text('EARLY', style: labelSmall),
        amount: Text(cardInfo.amount, style: labelMedium.copyWith(fontSize: 20)),
        action: ElevatedButton(
          onPressed: () {},
          style: ElevatedButton.styleFrom(
            backgroundColor: buttonColor,
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(8),
            ),
          ),
          child: Text(
            "PAY",
            style: TextStyle(color: buttonTextColor),
          ),
        ),
      ),
    );
  }
}


class _CardLayoutStrategy extends StatelessWidget {
  final Widget cardName,dueDate,amount,action,cardNo,labelEarly;
  const _CardLayoutStrategy({ required this.dueDate,
    required this.action, required this.cardName, required this.amount, required this.cardNo, required this.labelEarly});
  @override
  Widget build(BuildContext context) {
    return   (ColumnBuilder(arrangement: Arrangement.spaceBy(8))
        + (RowBuilder()+cardName.modifier(Modifier().weight(1))+cardNo).build()
        + VSpacer(24)
        + (RowBuilder()+dueDate.modifier(Modifier().align(Alignment.centerLeft).weight(1))+labelEarly).build()
        + (RowBuilder()+amount.modifier(Modifier().weight(1))+action).build())
        .build();
  }
}

class BillPayment {
  final String title;
  final IconData icon;

  BillPayment({
    required this.title,
    required this.icon,
  });
}
/**
 *-  This has nested scrollable behaviour, use it carefully with the consumer widget
 */
//@formater:off
class _PayBillSection extends StatelessWidget {
  final List<BillPayment> items;

  const _PayBillSection({super.key, required this.items});


  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text("Bill Payments", style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
        const SizedBox(height: 8),
        SingleChildScrollView(
          scrollDirection: Axis.horizontal,
          child: Row(
            children: items.map((item) =>
                Padding(padding: const EdgeInsets.symmetric(horizontal: 16.0,vertical: 4),
                    child: _PayBillItem(label: item.title, icon: item.icon))).toList(),
          ),
        ),
      ],
    );
  }
}
//@formatter:off
class _PayBillItem extends StatelessWidget {
  final String label;
  final IconData icon;

  const _PayBillItem({super.key, required this.label, required this.icon});

  @override
  Widget build(BuildContext context) {
    // Define the background color
    final backgroundColor = Color(0xFFF2F4F7);

    // Determine icon color based on background luminance
    final iconColor = backgroundColor.computeLuminance() > 0.5 ? Colors.black : Colors.white;

    return Container(
      child: Column(
        children: [
          IntrinsicHeight(
            child: Container(
              decoration: BoxDecoration(
                color: backgroundColor,
                borderRadius: BorderRadius.circular(8),
                boxShadow: [
                  BoxShadow(
                    color: Colors.black.withOpacity(0.1),
                    spreadRadius: 1,
                    blurRadius: 8,
                    offset: Offset(0, 1),
                  ),
                ],
              ),
              alignment: Alignment.center,
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Icon(
                  icon,
                  color: iconColor, // Set icon color based on background luminance
                ),
              ),
            ),
          ),
          const SizedBox(height: 8),
          Column(
            children: label.split(' ').map((word) => Text(
              word,
              style: TextStyle(fontSize: 14),
              textAlign: TextAlign.center,
            )).toList(),
          ),
        ],
      ),
    );
  }
}

/**
 *-  This has nested scrollable behaviour, use it carefully with the consumer widget
 */
//@formater:off
class _ActiveLoanSection extends StatelessWidget {
  final List<LoanItemData> loanItems;

  const _ActiveLoanSection({super.key, required this.loanItems});

  @override
  Widget build(BuildContext context) {

  return
    Container(
    height: 300,
      child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                Text("Active Loans", style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
                Spacer(),
                Text("See all ", style: TextStyle(fontSize: 16, fontWeight: FontWeight.w400)),
              ],
            ),
            const SizedBox(height: 8),
            SingleChildScrollView(
              scrollDirection: Axis.horizontal,
              physics: AlwaysScrollableScrollPhysics(),
              child: Row(
                children:loanItems.map((item)=>
                    _LoanItem(data: LoanItemData(
                      model: item.model,
                      imageLink:item.imageLink,
                      price:item.price,
                      date: item.date,
                      rating: item.rating,
                      ratingMax: item.ratingMax,
                    ))
                ).toList(),
              ),
            ),
          ],
        ),
  );

  }
}
//@formatter:off
class LoanItemData {
  final String model;
  final String imageLink;
  final String price;
  final String date;
  final int rating;
  final int ratingMax;

  const LoanItemData({
    required this.model,
    required this.imageLink,
    required this.price,
    required this.date,
    required this.rating,
    required this.ratingMax,
  });
}
//@formatter:off
class _LoanItem extends StatelessWidget {
  final LoanItemData data;

  const _LoanItem({super.key, required this.data});

  @override
  Widget build(BuildContext context) {
    final backgroundColor = Color(0xFFECEFF1);
    final textColor = ThemeData.estimateBrightnessForColor(backgroundColor) == Brightness.dark ? Colors.white : Colors.black;
    final labelMedium = TextStyle(fontSize: 16, fontWeight: FontWeight.w500, color: textColor);
    final progressColor = Colors.blueAccent;

    return Container(
      constraints: BoxConstraints(maxWidth: 280),
      decoration: BoxDecoration(
        color: backgroundColor,
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.1),
            blurRadius: 6,
            offset: Offset(0, 3),
          ),
        ],
        borderRadius: BorderRadius.circular(12),
      ),
      padding: EdgeInsets.all(12),
      child: _LoanItemLayoutStrategy(
        image: Container(width: 50, height: 50,
          color: Colors.white,
          alignment: Alignment.center,
          child: AsyncImage()
              .link(data.imageLink)
              .build()
              .modifier(Modifier().height(40)),
        ),
        price: Text(data.price, style: labelMedium),
        model: Text(data.model, style: labelMedium.copyWith(fontWeight: FontWeight.w400)),
        date: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text('Next', style: labelMedium.copyWith(fontWeight: FontWeight.w400, fontSize: 18)),
            SizedBox(height: 8),
            Text(data.date, style: labelMedium),
          ],
        ),
        rating: Row(
          children: [
            Text(data.rating.toString(), style: labelMedium.copyWith(color: progressColor)),
            Text("/${data.ratingMax}", style: labelMedium),
          ],
        ),
        ratingBar: LinearProgressIndicator(
          value: data.rating / data.ratingMax,
          color: progressColor,
          backgroundColor: Colors.grey.shade300,
        ),
      ),
    ).modifier(Modifier().padding(left: 8,right: 8))
    ;
  }
}


//@formatter:off
class _LoanItemLayoutStrategy extends StatelessWidget {
  final Widget price, model, date, rating, image, ratingBar;

  const _LoanItemLayoutStrategy({
    super.key,
    required this.price,
    required this.model,
    required this.date,
    required this.rating,
    required this.image,
    required this.ratingBar,
  });

  @override
  Widget build(BuildContext context) {
    return    Row(
      children: [
        image, SizedBox(width: 8),
        Expanded(
          child: Column(crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(children: [price, Spacer(), date]),
              SizedBox(height: 8),
              Row(children: [model, Spacer(), rating]),
              SizedBox(height: 8),
              ratingBar,
            ],
          ),
        ),
      ],
    );

  }
}



