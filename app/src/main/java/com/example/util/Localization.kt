package com.example.util

import com.example.data.model.AppLanguage
import com.example.data.model.ExpenseCategory

object Localization {

    fun getCategoryName(category: ExpenseCategory, lang: AppLanguage): String {
        return when (lang) {
            AppLanguage.ENGLISH -> when (category) {
                ExpenseCategory.FOOD_GROCERIES -> "Groceries & Food"
                ExpenseCategory.DINING -> "Dining & Cafe"
                ExpenseCategory.TRANSPORT_FUEL -> "Transport & Fuel"
                ExpenseCategory.SHOPPING -> "Shopping & Clothes"
                ExpenseCategory.UTILITIES_BILLS -> "Bills & Utilities"
                ExpenseCategory.RENT_HOUSING -> "Rent & Housing"
                ExpenseCategory.HEALTH_MEDICAL -> "Health & Medical"
                ExpenseCategory.ENTERTAINMENT -> "Entertainment"
                ExpenseCategory.EDUCATION -> "Education"
                ExpenseCategory.INVESTMENT -> "Investment"
                ExpenseCategory.SALARY -> "Salary"
                ExpenseCategory.BUSINESS -> "Business Income"
                ExpenseCategory.OTHER -> "Other"
            }
            AppLanguage.HINDI -> when (category) {
                ExpenseCategory.FOOD_GROCERIES -> "किराना और राशन"
                ExpenseCategory.DINING -> "होटल और खान-पान"
                ExpenseCategory.TRANSPORT_FUEL -> "यातायात और पेट्रोल"
                ExpenseCategory.SHOPPING -> "खरीदारी और कपड़े"
                ExpenseCategory.UTILITIES_BILLS -> "बिजली/वाई-फाई बिल"
                ExpenseCategory.RENT_HOUSING -> "घर का किराया"
                ExpenseCategory.HEALTH_MEDICAL -> "स्वास्थ्य और दवाई"
                ExpenseCategory.ENTERTAINMENT -> "मनोरंजन और मूवी"
                ExpenseCategory.EDUCATION -> "शिक्षा और फीस"
                ExpenseCategory.INVESTMENT -> "निवेश और बचत"
                ExpenseCategory.SALARY -> "मासिक वेतन"
                ExpenseCategory.BUSINESS -> "व्यापार आय"
                ExpenseCategory.OTHER -> "अन्य खर्च"
            }
            AppLanguage.TELUGU -> when (category) {
                ExpenseCategory.FOOD_GROCERIES -> "కిరాణా & ఆహారం"
                ExpenseCategory.DINING -> "హోటల్ & భోజనం"
                ExpenseCategory.TRANSPORT_FUEL -> "ప్రయాణం & పెట్రోల్"
                ExpenseCategory.SHOPPING -> "షాపింగ్ & వస్త్రాలు"
                ExpenseCategory.UTILITIES_BILLS -> "కరెంట్ & బిల్లులు"
                ExpenseCategory.RENT_HOUSING -> "ఇంటి అద్దె"
                ExpenseCategory.HEALTH_MEDICAL -> "ఆరోగ్యం & మందులు"
                ExpenseCategory.ENTERTAINMENT -> "వినోదం & సినిమాలు"
                ExpenseCategory.EDUCATION -> "చదువు & ఫీజులు"
                ExpenseCategory.INVESTMENT -> "పెట్టుబడులు"
                ExpenseCategory.SALARY -> "నెలవారీ జీతం"
                ExpenseCategory.BUSINESS -> "వ్యాపార ఆదాయం"
                ExpenseCategory.OTHER -> "ఇతరములు"
            }
        }
    }

    // UI Strings map
    fun s(key: String, lang: AppLanguage): String {
        val dict = when (lang) {
            AppLanguage.ENGLISH -> englishStrings
            AppLanguage.HINDI -> hindiStrings
            AppLanguage.TELUGU -> teluguStrings
        }
        return dict[key] ?: englishStrings[key] ?: key
    }

    private val englishStrings = mapOf(
        "app_title" to "Expense Tracker",
        "home" to "Home",
        "transactions" to "Transactions",
        "analytics" to "Analytics",
        "shopping" to "Shopping List",
        "favorites" to "Quick Add",
        "total_balance" to "Total Balance",
        "total_income" to "Total Income",
        "total_expense" to "Total Expense",
        "this_month" to "This Month",
        "recent_transactions" to "Recent Transactions",
        "view_all" to "View All",
        "add_expense" to "Add Expense",
        "add_income" to "Add Income",
        "new_transaction" to "New Transaction",
        "title" to "Title",
        "title_hint" to "e.g., Grocery shopping, Metro card",
        "amount" to "Amount",
        "category" to "Category",
        "payment_method" to "Payment Method",
        "notes" to "Notes / Details",
        "save" to "Save",
        "cancel" to "Cancel",
        "search_hint" to "Search by title, note, or amount...",
        "filter_all" to "All",
        "filter_expense" to "Expenses",
        "filter_income" to "Income",
        "filter_today" to "Today",
        "filter_month" to "This Month",
        "monthly_budget" to "Monthly Budget",
        "budget_used" to "Budget Spent",
        "budget_remaining" to "Remaining",
        "set_budget" to "Set Monthly Budget",
        "spending_by_category" to "Spending Breakdown",
        "voice_log" to "Voice Log",
        "voice_hint" to "Say or type: 'Spent 250 on vegetables' or 'Petrol 500'",
        "listening" to "Listening...",
        "parsed_preview" to "Detected Transaction:",
        "confirm_add" to "Confirm & Add",
        "shopping_title" to "Shopping & Expense Planner",
        "shopping_subtitle" to "Plan purchases, estimate total cost, and log items directly",
        "add_item" to "Add Item",
        "item_name" to "Item Name",
        "quantity" to "Quantity (e.g. 2 kg, 1 pack)",
        "est_price" to "Est. Price",
        "actual_price" to "Actual Price",
        "total_estimated" to "Total Estimated",
        "total_actual" to "Total Spent",
        "mark_bought" to "Mark Purchased",
        "log_as_expense" to "Log to Expenses",
        "clear_purchased" to "Clear Purchased",
        "frequent_expenses" to "1-Tap Quick Presets",
        "frequent_hint" to "Instantly record regular everyday spends in one tap",
        "create_preset" to "New Preset",
        "empty_transactions" to "No transactions found yet. Tap '+' or mic to record!",
        "empty_shopping" to "Your shopping list is empty. Add grocery or household items!",
        "language" to "Language",
        "currency" to "Currency",
        "budget_alert_over" to "Alert: You have exceeded your monthly budget!",
        "budget_alert_warning" to "Warning: You have used over 80% of your budget.",
        "delete" to "Delete",
        "edit" to "Edit",
        "success_added" to "Transaction added successfully!"
    )

    private val hindiStrings = mapOf(
        "app_title" to "खर्च ट्रैकर",
        "home" to "होम",
        "transactions" to "लेन-देन",
        "analytics" to "विश्लेषण",
        "shopping" to "खरीदारी सूची",
        "favorites" to "त्वरित जोड़ें",
        "total_balance" to "कुल शेष राशि",
        "total_income" to "कुल आय",
        "total_expense" to "कुल खर्च",
        "this_month" to "इस महीने",
        "recent_transactions" to "हाल के लेन-देन",
        "view_all" to "सभी देखें",
        "add_expense" to "खर्च जोड़ें",
        "add_income" to "आय जोड़ें",
        "new_transaction" to "नया लेन-देन",
        "title" to "शीर्षक",
        "title_hint" to "उदा. किराना सामान, पेट्रोल, नाश्ता",
        "amount" to "राशि",
        "category" to "श्रेणी",
        "payment_method" to "भुगतान विधि",
        "notes" to "विवरण / नोट",
        "save" to "सुरक्षित करें",
        "cancel" to "रद्द करें",
        "search_hint" to "नाम, नोट या राशि से खोजें...",
        "filter_all" to "सभी",
        "filter_expense" to "खर्च",
        "filter_income" to "आय",
        "filter_today" to "आज",
        "filter_month" to "इस माह",
        "monthly_budget" to "मासिक बजट",
        "budget_used" to "बजट खर्च",
        "budget_remaining" to "बचा हुआ बजट",
        "set_budget" to "मासिक बजट सेट करें",
        "spending_by_category" to "श्रेणीवार खर्च विवरण",
        "voice_log" to "आवाज से जोड़ें",
        "voice_hint" to "बोलें: 'सब्जी 250 रुपये' या 'पेट्रोल 500'",
        "listening" to "सुन रहे हैं...",
        "parsed_preview" to "पहचाना गया खर्च:",
        "confirm_add" to "पुष्टि कर जोड़ें",
        "shopping_title" to "खरीदारी और बजट योजना",
        "shopping_subtitle" to "सामान की सूची बनाएं, लागत का अनुमान लगाएं और खर्च में जोड़ें",
        "add_item" to "सामान जोड़ें",
        "item_name" to "सामान का नाम",
        "quantity" to "मात्रा (उदा. 2 किलो, 1 पैकेट)",
        "est_price" to "अनुमानित कीमत",
        "actual_price" to "वास्तविक कीमत",
        "total_estimated" to "कुल अनुमानित",
        "total_actual" to "कुल खरीदारी",
        "mark_bought" to "खरीदा गया मार्क करें",
        "log_as_expense" to "खर्च में दर्ज करें",
        "clear_purchased" to "खरीदी गई वस्तुएं हटाएं",
        "frequent_expenses" to "1-टैप त्वरित खर्च",
        "frequent_hint" to "दैनिक नियमित खर्चों को केवल एक टैप में दर्ज करें",
        "create_preset" to "नया शॉर्टकट",
        "empty_transactions" to "अभी कोई लेन-देन नहीं है। '+' या माइक दबाकर जोड़ें!",
        "empty_shopping" to "आपकी खरीदारी सूची खाली है। नया सामान जोड़ें!",
        "language" to "भाषा (Language)",
        "currency" to "मुद्रा (Currency)",
        "budget_alert_over" to "चेतावनी: आपका मासिक बजट समाप्त हो चुका है!",
        "budget_alert_warning" to "ध्यान दें: आपने 80% से अधिक बजट उपयोग कर लिया है।",
        "delete" to "हटाएं",
        "edit" to "संपादित करें",
        "success_added" to "सफलतापूर्वक दर्ज किया गया!"
    )

    private val teluguStrings = mapOf(
        "app_title" to "ఖర్చుల ట్రాకర్",
        "home" to "హోమ్",
        "transactions" to "లావాదేవీలు",
        "analytics" to "విశ్లేషణ",
        "shopping" to "షాపింగ్ జాబితా",
        "favorites" to "త్వరిత ఎంట్రీ",
        "total_balance" to "మొత్తం నిల్వ",
        "total_income" to "మొత్తం ఆదాయం",
        "total_expense" to "మొత్తం ఖర్చు",
        "this_month" to "ఈ నెల",
        "recent_transactions" to "ఇటీవలి లావాదేవీలు",
        "view_all" to "అన్నీ చూడండి",
        "add_expense" to "ఖర్చు జోడించండి",
        "add_income" to "ఆదాయం జోడించండి",
        "new_transaction" to "కొత్త లావాదేవీ",
        "title" to "వివరణ పేరు",
        "title_hint" to "ఉదా. కూరగాయలు, పెట్రోల్, టిఫిన్",
        "amount" to "మొత్తం (రూ)",
        "category" to "వర్గం (Category)",
        "payment_method" to "చెల్లింపు పద్ధతి",
        "notes" to "వివరాలు / నోట్స్",
        "save" to "భద్రపరుచు",
        "cancel" to "రద్దు చేయి",
        "search_hint" to "పేరు లేదా మొత్తంతో వెతకండి...",
        "filter_all" to "అన్నీ",
        "filter_expense" to "ఖర్చులు",
        "filter_income" to "ఆదాయం",
        "filter_today" to "ఈ రోజు",
        "filter_month" to "ఈ నెల",
        "monthly_budget" to "నెలవారీ బడ్జెట్",
        "budget_used" to "ఖర్చయిన బడ్జెట్",
        "budget_remaining" to "మిగిలిన బడ్జెట్",
        "set_budget" to "నెల బడ్జెట్ నిర్ణయించు",
        "spending_by_category" to "వర్గాల వారీగా ఖర్చులు",
        "voice_log" to "వాయిస్ ద్వారా నమోదు",
        "voice_hint" to "మాట్లాడండి: 'కూరగాయలు 250' లేదా 'పెట్రోల్ 500'",
        "listening" to "వింటున్నాము...",
        "parsed_preview" to "గుర్తించిన ఖర్చు:",
        "confirm_add" to "ధృవీకరించి జోడించు",
        "shopping_title" to "షాపింగ్ మరియు బడ్జెట్ ప్లానర్",
        "shopping_subtitle" to "కొనుగోళ్ల జాబితా రూపొందించి, ఖర్చులను లెక్కించండి",
        "add_item" to "వస్తువును జోడించు",
        "item_name" to "వస్తువు పేరు",
        "quantity" to "పరిమాణం (ఉదా. 2 కేజీలు, 1 ప్యాకెట్)",
        "est_price" to "అంచనా ధర",
        "actual_price" to "అసలు ధర",
        "total_estimated" to "మొత్తం అంచనా",
        "total_actual" to "మొత్తం ఖర్చు",
        "mark_bought" to "కొనుగోలు చేసినట్లు గుర్తించు",
        "log_as_expense" to "ఖర్చులలోకి జోడించు",
        "clear_purchased" to "కొన్నవి తీసివేయి",
        "frequent_expenses" to "1-ట్యాప్ శీఘ్ర ఖర్చులు",
        "frequent_hint" to "రోజూ చేసే సాధారణ ఖర్చులను ఒక్క ట్యాప్‌తో నమోదు చేయండి",
        "create_preset" to "కొత్త షార్ట్‌కట్",
        "empty_transactions" to "లావాదేవీలు ఏవీ లేవు. '+' లేదా మైక్ నొక్కి జోడించండి!",
        "empty_shopping" to "షాపింగ్ జాబితా ఖాళీగా ఉంది. కొత్త వస్తువులను జోడించండి!",
        "language" to "భాష (Language)",
        "currency" to "కరెన్సీ (Currency)",
        "budget_alert_over" to "హెచ్చరిక: మీ నెలవారీ బడ్జెట్ పరిమితి దాటిపోయింది!",
        "budget_alert_warning" to "గమనిక: మీరు 80% బడ్జెట్ ఉపయోగించారు.",
        "delete" to "తొలగించు",
        "edit" to "సవరించు",
        "success_added" to "విజయవంతంగా నమోదు చేయబడింది!"
    )
}
