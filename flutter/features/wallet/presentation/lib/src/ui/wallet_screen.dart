import 'package:ComposableWidget/composable_widget.dart';
import 'package:core_ui/core_ui_api.dart';
import 'package:flutter/material.dart';
import 'package:wallet_domain/domain_api.dart';
import '../presentation_logic/factory/factory.dart';


//@formatter:off
class WalletScreen extends StatelessWidget {
  final controller=PresentationFactory.createController();
  WalletScreen(){
    //TODO:Not good idea to fetch data within UI, refactor later
    controller.read();
  }
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        leading:IconButton(icon: Icon(Icons.arrow_back), onPressed: () {}) ,
        title: Text('Wallet',style: TextStyle(fontWeight: FontWeight.w500,fontSize: 16)),
        actions:  [IconButton(icon: Icon(Icons.account_circle), onPressed: () {}).modifier(Modifier().padding(right: 16))],
      ),
      body: SingleChildScrollView(
        child: Column(
          children: [
            StreamBuilderStrategyWithSnackBar<SpendData?>(
                messageStream: controller.statusMessage,
                dataStream: controller.spendData,
                builder: (context, snapshot) {
                  final data = snapshot.data;
                  if (data == null) return EmptyContentScreen();
                  return   _Bars(period:data.period, typeOfCost: 'Spend Chart',
                      costs:data.spend.data.firstOrNull?.toCost()??[], currencyType:data.currency);
                }),
            StreamBuilderStrategyWithSnackBar<List<BreakdownItemData>>(
                messageStream: controller.statusMessage,
                dataStream: controller.breakdowns,
                builder: (context, snapshot) {
                  final data = snapshot.data;
                  if (data == null) return EmptyContentScreen();
                  return   _BreakDown(itemData:data);
                }),

            SizedBox(height: 16),
            (ColumnBuilder(modifier:Modifier().linearGradient([Color(0xFFF8F8F8), Color(0xFFFFFFFF)]))

                +  Card(color: Colors.white,
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(64)),elevation: 1,
                  child: _AccountInfo(expiryDate: "12/24", ccv: "123",
                    onDeletePressed: () {}, onSkipPressed: () {}, onAccountNumberChanged: (value) {}, onPasswordChanged: (value) {},
                  ).modifier(Modifier().paddingAll(32)),
                )
            ).build()
          ],
        ),
      ),
    );
  }
}

//@formatter:off
class _Bars extends StatelessWidget {
  final String period,typeOfCost, currencyType; final List<double> costs;

  _Bars({required this.period, required this.typeOfCost, required this.costs, this.currencyType = '\$',});

  @override
  Widget build(BuildContext context) {
    return Column(mainAxisSize: MainAxisSize.min, crossAxisAlignment: CrossAxisAlignment.start, // Ensures all text is left-aligned
      children: [
        // Align text and bar chart together
        Column(crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              '$period',
              style: Theme.of(context).textTheme.bodyMedium!.copyWith(fontSize: 14,
              color: Theme.of(context).colorScheme.onSurface.withOpacity(0.6))),

            SizedBox(height: 4),
            Text(
              '$typeOfCost',
              style: Theme.of(context).textTheme.headlineSmall!.copyWith(fontSize: 18, fontWeight: FontWeight.bold,
                    color: Theme.of(context).colorScheme.onSurface.withOpacity(0.9)),
            ),
          ],
        ),
        SizedBox(height: 16),
        // Display the bar chart, aligned with the text above
        _BarChart(costs: costs, currencySymbol: currencyType),
      ],
    );
  }
}
//@formatter:off
class _BarChart extends StatelessWidget {
  final List<double> costs; final String currencySymbol;

  _BarChart({required this.costs, required this.currencySymbol});

  @override
  Widget build(BuildContext context) {
    final maxCost = costs.reduce((a, b) => a > b ? a : b);

    return Row(mainAxisSize: MainAxisSize.min, crossAxisAlignment: CrossAxisAlignment.end,
      children: costs.map((cost) {
        return Padding(padding: const EdgeInsets.symmetric(horizontal: 4.0),
          child: _Bar(cost: cost, maxCost: maxCost, barColor: const Color(0xFF7F00FF), currencySymbol: currencySymbol),
        );
      }).toList(),
    );
  }
}

//@formatter:off
class _Bar extends StatelessWidget {
  final double cost, maxCost;final Color barColor;final String currencySymbol;

  _Bar({required this.cost, required this.maxCost, this.barColor = const Color(0xFF7F00FF), this.currencySymbol = '\$'});

  @override
  Widget build(BuildContext context) {
    // Scale the height based on maxCost to fit within a fixed height (e.g., 200 pixels)
    final double barHeight = (150 * cost) / maxCost;

    return IntrinsicWidth(
      child: Column(mainAxisAlignment: MainAxisAlignment.end,
        children: [
          Container(height: barHeight, decoration: BoxDecoration(
              color: barColor, // Use the custom or default bar color
              borderRadius: BorderRadius.only(topLeft: Radius.circular(8.0), topRight: Radius.circular(8.0)),
              boxShadow: [BoxShadow(color: Colors.black.withOpacity(0.2), spreadRadius: 1, blurRadius: 4, offset: Offset(0, 2))],
            ),
          ),
          SizedBox(height: 8),
          // Display cost below the bar with currency symbol
          Padding(padding: const EdgeInsets.only(top: 8.0, right: 8.0, bottom: 8.0),
            child: Text('$currencySymbol${cost.toStringAsFixed(2)}', style: TextStyle(fontSize: 12), textAlign: TextAlign.start),
          )
        ],
      ),
    );
  }
}


/**
 *-  This has nested scrollable behaviour, use it carefully with the consumer widget
 */
//@formater:off
class _BreakDown extends StatelessWidget {
  final List<BreakdownItemData> itemData;

  const _BreakDown({required this.itemData});

  List<_BreakdownItem> get sortedItems {
    final items = itemData.map((data) {
      return _BreakdownItem(
        percentage: data.percentage,
        label: data.label,
        position: 0, // temporary placeholder for position, will be updated after sorting
      );
    }).toList();

    items.sort((a, b) {
      final aPercentage = double.tryParse(a.percentage.replaceAll('%', '')) ?? 0.0;
      final bPercentage = double.tryParse(b.percentage.replaceAll('%', '')) ?? 0.0;
      return bPercentage.compareTo(aPercentage);
    });

    return items.asMap().entries.map((entry) {
      final index = entry.key; final item = entry.value;
      return _BreakdownItem(percentage: item.percentage, label: item.label, position: index);
    }).toList();
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text("Breakdown", style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
        const SizedBox(height: 8),
        SingleChildScrollView(
          scrollDirection: Axis.horizontal,
          child: Row(
            children: sortedItems.map((item) => Padding(padding: const EdgeInsets.symmetric(horizontal: 8.0),child: item)).toList(),
          ),
        ),
      ],
    );
  }
}

//@formater:off
class _BreakdownItem extends StatelessWidget {
  final String percentage, label;final Color circleColor, percentageTextColor, labelColor;
  final double labelOpacity;final int position;

  const _BreakdownItem({required this.percentage, required this.label, this.circleColor = Colors.white, this.percentageTextColor = Colors.red,
    this.labelColor = Colors.grey, this.labelOpacity = 0.6, required this.position});

  @override
  Widget build(BuildContext context) {
    return IntrinsicWidth(
      child: Column(
        children: [
          IntrinsicHeight(
            child: Container(
              decoration: BoxDecoration(color: circleColor, shape: BoxShape.circle,
                boxShadow: [
                  BoxShadow(color: Colors.black.withOpacity(0.1), spreadRadius: 1, blurRadius: 8, offset: Offset(0, 1)),
                ],
              ),
              alignment: Alignment.center,
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Text(
                  percentage,
                  style: TextStyle(color: percentageTextColor.withOpacity(_calculateOpacity()), fontSize: 18, fontWeight: FontWeight.w500),
                ),
              ),
            ),
          ),
          const SizedBox(height: 8),
          Text(label, style: TextStyle(color: labelColor.withOpacity(labelOpacity), fontSize: 14),
          ),
        ],
      ),
    );
  }

  double _calculateOpacity() {
    final baseOpacity = 1.0;final decrement = 0.1;
    return (baseOpacity - (position * decrement)).clamp(0.1, 1.0);
  }
}


//@formater:off
class _AccountInfo extends StatelessWidget {
  final String expiryDate; final String ccv; final VoidCallback onDeletePressed;
  final VoidCallback onSkipPressed; final ValueChanged<String> onAccountNumberChanged; final ValueChanged<String> onPasswordChanged;

  const _AccountInfo({super.key, required this.expiryDate, required this.ccv,
    required this.onDeletePressed, required this.onSkipPressed, required this.onAccountNumberChanged, required this.onPasswordChanged});

  @override
  Widget build(BuildContext context) {
    const labelTextStyle = TextStyle(fontSize: 14, fontWeight: FontWeight.w500);

    return ConstrainedBox(
      constraints: BoxConstraints(maxWidth: 400),
      child: _LayoutStrategy(
        labelAccount: Text('Account Number:', style: labelTextStyle),
        labelPassword: Text('Password:', style: labelTextStyle),
        expireDate: Text('Expire Date: $expiryDate', style: labelTextStyle),
        ccv: Text('CCV: $ccv', style: labelTextStyle),

        fieldAccountNo: SizedBox(
          width: 200,
          child: _TextField(
            textStyle: labelTextStyle,
            controller: TextEditingController(),
            visualTransformer: _accountNumberTransformer,
            onChanged: onAccountNumberChanged,
          ),
        ),

        fieldPassword: SizedBox(
          width: 200,
          child: _TextField(
            obscureText: true,
            textStyle: labelTextStyle,
            onChanged: onPasswordChanged, // Callback for password changes
          ),
        ),

        deleteButton: ElevatedButton(
          onPressed: onDeletePressed,
          style: ElevatedButton.styleFrom(
            backgroundColor: Color(0xFF7F00FF),
            padding: EdgeInsets.symmetric(horizontal: 12.0, vertical: 4.0),
          ),
          child: Text('Delete Card', style: TextStyle(color: Colors.white)),
        ),

        skipButton: ElevatedButton(
          onPressed: onSkipPressed,
          style: ElevatedButton.styleFrom(
            backgroundColor: Colors.blue,
            padding: EdgeInsets.symmetric(vertical: 12.0),
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(4),
            ),
          ),
          child: Padding(
            padding: const EdgeInsets.all(6.0),
            child: Text('SKIP', style: TextStyle(color: Colors.white, fontSize: 16)),
          ),
        ),
      ),
    );
  }
}





// @formatter:off
class _TextField extends StatelessWidget {
  final TextEditingController? controller;
  final String? hintText;
  final bool obscureText;
  final int? maxLength;
  final ValueChanged<String>? onChanged;
  final InputDecoration? decoration;
  final TextCapitalization textCapitalization;
  final TextInputType keyboardType;
  final TextInputAction? textInputAction;
  final ValueTransformer? visualTransformer;
  final TextStyle textStyle;

  const _TextField({
    super.key,
    this.controller,
    this.hintText,
    this.obscureText = false,
    this.maxLength,
    this.onChanged,
    this.decoration,
    this.textCapitalization = TextCapitalization.none,
    this.keyboardType = TextInputType.text,
    this.textInputAction,
    this.visualTransformer,
    this.textStyle = const TextStyle(fontSize: 16),
  });

  @override
  Widget build(BuildContext context) {
    // Apply visual transformation initially
    controller?.addListener(() {
      final originalText = controller!.text.replaceAll(' ', '');
      final transformedText = visualTransformer?.call(originalText) ?? originalText;

      if (controller!.text != transformedText) {
        controller!.value = TextEditingValue(
          text: transformedText,
          selection: TextSelection.collapsed(offset: transformedText.length),
        );
      }
    });

    return TextField(
      controller: controller,
      obscureText: obscureText,
      maxLength: maxLength,
      decoration: decoration ??
          InputDecoration(
            hintText: hintText,
            isDense: true,
            contentPadding: EdgeInsets.symmetric(vertical: 8.0, horizontal: 12.0),
            border: OutlineInputBorder(
              borderRadius: BorderRadius.circular(20.0),
            ),
          ),
      textCapitalization: textCapitalization,
      keyboardType: keyboardType,
      textInputAction: textInputAction,
      style: textStyle,
      onChanged: onChanged, // Trigger the external onChanged callback with raw text if needed
    );
  }
}




typedef ValueTransformer = String Function(String);
String _accountNumberTransformer(String input) {
  // Remove any existing spaces and non-digit characters from the input
  final cleanedInput = input.replaceAll(RegExp(r'\D'), '');

  final buffer = StringBuffer();
  for (int i = 0; i < cleanedInput.length; i++) {
    if (i > 0 && i % 4 == 0) {
      buffer.write(' ');
    }
    buffer.write(cleanedInput[i]);
  }

  return buffer.toString();
}



//@formatter:off
class _LayoutStrategy extends StatelessWidget {
  final Widget labelAccount,labelPassword,expireDate,ccv,fieldAccountNo,fieldPassword,deleteButton,skipButton;

  const _LayoutStrategy({super.key, required this.labelAccount, required this.labelPassword, required this.expireDate,
    required this.ccv, required this.fieldAccountNo, required this.fieldPassword,
    required this.deleteButton, required this.skipButton});

  @override
  Widget build(BuildContext context) {
   return (ColumnBuilder(arrangement: Arrangement.spaceBy(8))
   + (RowBuilder()+labelAccount.modifier(Modifier().weight(1))+fieldAccountNo).build()
   + (RowBuilder()+labelPassword.modifier(Modifier().weight(1))+fieldPassword).build()
   + _CardInfoLayoutStrategy(expireDate: expireDate,ccv: ccv,deleteButton: deleteButton)
   +VSpacer(8)
   + skipButton.modifier(Modifier().fillMaxWidth()))
   .build();

  }
}
//@formatter:off
class _CardInfoLayoutStrategy extends StatelessWidget {
  final Widget expireDate,ccv,deleteButton;

  const _CardInfoLayoutStrategy({super.key, required this.expireDate, required this.ccv, required this.deleteButton});


  @override
  Widget build(BuildContext context) =>(RowBuilder()
        + (ColumnBuilder(horizontalAlignment: CrossAxisAlignment.start)+expireDate+VSpacer(4)+ccv).build().modifier(Modifier().weight(1))
        + deleteButton.modifier(Modifier().align(Alignment.centerRight))).build();
}

