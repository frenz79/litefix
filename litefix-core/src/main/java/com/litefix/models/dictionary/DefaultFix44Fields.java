package com.litefix.models.dictionary;

import com.litefix.models.fixmessage.FixMessageDictionary.FieldType;
import java.util.HashMap;
import java.util.Map;

// Generated at:2025-02-17T10:03:26.877543600Z[UTC]

public class DefaultFix44Fields {

public FixField of(int tag) { return fieldsByTag[tag]; }
public FixField of(String name) { return fieldsByName.get(name); }

public void add( FixField f ) {
fieldsByTag[f.tag()] = f;
fieldsByName.put(f.name(), f);
}


public static final FixField Account                   = new FixField("Account"                ,   1, FieldType.STRING       );
public static final FixField AdvId                   = new FixField("AdvId"                ,   2, FieldType.STRING       );
public static final FixField AdvRefID                   = new FixField("AdvRefID"                ,   3, FieldType.STRING       );
public static final FixField AdvSide                   = new FixField("AdvSide"                ,   4, FieldType.CHAR       );
public static final FixField AdvTransType                   = new FixField("AdvTransType"                ,   5, FieldType.STRING       );
public static final FixField AvgPx                   = new FixField("AvgPx"                ,   6, FieldType.DECIMAL       );
public static final FixField BeginSeqNo                   = new FixField("BeginSeqNo"                ,   7, FieldType.INTEGER       );
public static final FixField BeginString                   = new FixField("BeginString"                ,   8, FieldType.STRING       );
public static final FixField BodyLength                   = new FixField("BodyLength"                ,   9, FieldType.INTEGER       );
public static final FixField CheckSum                   = new FixField("CheckSum"                ,   10, FieldType.STRING       );
public static final FixField ClOrdID                   = new FixField("ClOrdID"                ,   11, FieldType.STRING       );
public static final FixField Commission                   = new FixField("Commission"                ,   12, FieldType.DECIMAL       );
public static final FixField CommType                   = new FixField("CommType"                ,   13, FieldType.CHAR       );
public static final FixField CumQty                   = new FixField("CumQty"                ,   14, FieldType.DECIMAL       );
public static final FixField Currency                   = new FixField("Currency"                ,   15, FieldType.STRING       );
public static final FixField EndSeqNo                   = new FixField("EndSeqNo"                ,   16, FieldType.INTEGER       );
public static final FixField ExecID                   = new FixField("ExecID"                ,   17, FieldType.STRING       );
public static final FixField ExecInst                   = new FixField("ExecInst"                ,   18, FieldType.STRING       );
public static final FixField ExecRefID                   = new FixField("ExecRefID"                ,   19, FieldType.STRING       );
public static final FixField HandlInst                   = new FixField("HandlInst"                ,   21, FieldType.CHAR       );
public static final FixField SecurityIDSource                   = new FixField("SecurityIDSource"                ,   22, FieldType.STRING       );
public static final FixField IOIID                   = new FixField("IOIID"                ,   23, FieldType.STRING       );
public static final FixField IOIQltyInd                   = new FixField("IOIQltyInd"                ,   25, FieldType.CHAR       );
public static final FixField IOIRefID                   = new FixField("IOIRefID"                ,   26, FieldType.STRING       );
public static final FixField IOIQty                   = new FixField("IOIQty"                ,   27, FieldType.STRING       );
public static final FixField IOITransType                   = new FixField("IOITransType"                ,   28, FieldType.CHAR       );
public static final FixField LastCapacity                   = new FixField("LastCapacity"                ,   29, FieldType.CHAR       );
public static final FixField LastMkt                   = new FixField("LastMkt"                ,   30, FieldType.STRING       );
public static final FixField LastPx                   = new FixField("LastPx"                ,   31, FieldType.DECIMAL       );
public static final FixField LastQty                   = new FixField("LastQty"                ,   32, FieldType.DECIMAL       );
public static final FixField LinesOfText                   = new FixField("LinesOfText"                ,   33, FieldType.GROUP_SIZE       );
public static final FixField MsgSeqNum                   = new FixField("MsgSeqNum"                ,   34, FieldType.INTEGER       );
public static final FixField MsgType                   = new FixField("MsgType"                ,   35, FieldType.STRING       );
public static final FixField NewSeqNo                   = new FixField("NewSeqNo"                ,   36, FieldType.INTEGER       );
public static final FixField OrderID                   = new FixField("OrderID"                ,   37, FieldType.STRING       );
public static final FixField OrderQty                   = new FixField("OrderQty"                ,   38, FieldType.DECIMAL       );
public static final FixField OrdStatus                   = new FixField("OrdStatus"                ,   39, FieldType.CHAR       );
public static final FixField OrdType                   = new FixField("OrdType"                ,   40, FieldType.CHAR       );
public static final FixField OrigClOrdID                   = new FixField("OrigClOrdID"                ,   41, FieldType.STRING       );
public static final FixField OrigTime                   = new FixField("OrigTime"                ,   42, FieldType.UTC_TIMESTAMP       );
public static final FixField PossDupFlag                   = new FixField("PossDupFlag"                ,   43, FieldType.BOOLEAN       );
public static final FixField Price                   = new FixField("Price"                ,   44, FieldType.DECIMAL       );
public static final FixField RefSeqNum                   = new FixField("RefSeqNum"                ,   45, FieldType.INTEGER       );
public static final FixField SecurityID                   = new FixField("SecurityID"                ,   48, FieldType.STRING       );
public static final FixField SenderCompID                   = new FixField("SenderCompID"                ,   49, FieldType.STRING       );
public static final FixField SenderSubID                   = new FixField("SenderSubID"                ,   50, FieldType.STRING       );
public static final FixField SendingTime                   = new FixField("SendingTime"                ,   52, FieldType.UTC_TIMESTAMP       );
public static final FixField Quantity                   = new FixField("Quantity"                ,   53, FieldType.DECIMAL       );
public static final FixField Side                   = new FixField("Side"                ,   54, FieldType.CHAR       );
public static final FixField Symbol                   = new FixField("Symbol"                ,   55, FieldType.STRING       );
public static final FixField TargetCompID                   = new FixField("TargetCompID"                ,   56, FieldType.STRING       );
public static final FixField TargetSubID                   = new FixField("TargetSubID"                ,   57, FieldType.STRING       );
public static final FixField Text                   = new FixField("Text"                ,   58, FieldType.STRING       );
public static final FixField TimeInForce                   = new FixField("TimeInForce"                ,   59, FieldType.CHAR       );
public static final FixField TransactTime                   = new FixField("TransactTime"                ,   60, FieldType.UTC_TIMESTAMP       );
public static final FixField Urgency                   = new FixField("Urgency"                ,   61, FieldType.CHAR       );
public static final FixField ValidUntilTime                   = new FixField("ValidUntilTime"                ,   62, FieldType.UTC_TIMESTAMP       );
public static final FixField SettlType                   = new FixField("SettlType"                ,   63, FieldType.CHAR       );
public static final FixField SettlDate                   = new FixField("SettlDate"                ,   64, FieldType.UTC_TIMESTAMP       );
public static final FixField SymbolSfx                   = new FixField("SymbolSfx"                ,   65, FieldType.STRING       );
public static final FixField ListID                   = new FixField("ListID"                ,   66, FieldType.STRING       );
public static final FixField ListSeqNo                   = new FixField("ListSeqNo"                ,   67, FieldType.INTEGER       );
public static final FixField TotNoOrders                   = new FixField("TotNoOrders"                ,   68, FieldType.INTEGER       );
public static final FixField ListExecInst                   = new FixField("ListExecInst"                ,   69, FieldType.STRING       );
public static final FixField AllocID                   = new FixField("AllocID"                ,   70, FieldType.STRING       );
public static final FixField AllocTransType                   = new FixField("AllocTransType"                ,   71, FieldType.CHAR       );
public static final FixField RefAllocID                   = new FixField("RefAllocID"                ,   72, FieldType.STRING       );
public static final FixField NoOrders                   = new FixField("NoOrders"                ,   73, FieldType.GROUP_SIZE       );
public static final FixField AvgPxPrecision                   = new FixField("AvgPxPrecision"                ,   74, FieldType.INTEGER       );
public static final FixField TradeDate                   = new FixField("TradeDate"                ,   75, FieldType.UTC_TIMESTAMP       );
public static final FixField PositionEffect                   = new FixField("PositionEffect"                ,   77, FieldType.CHAR       );
public static final FixField NoAllocs                   = new FixField("NoAllocs"                ,   78, FieldType.GROUP_SIZE       );
public static final FixField AllocAccount                   = new FixField("AllocAccount"                ,   79, FieldType.STRING       );
public static final FixField AllocQty                   = new FixField("AllocQty"                ,   80, FieldType.DECIMAL       );
public static final FixField ProcessCode                   = new FixField("ProcessCode"                ,   81, FieldType.CHAR       );
public static final FixField NoRpts                   = new FixField("NoRpts"                ,   82, FieldType.GROUP_SIZE       );
public static final FixField RptSeq                   = new FixField("RptSeq"                ,   83, FieldType.INTEGER       );
public static final FixField CxlQty                   = new FixField("CxlQty"                ,   84, FieldType.DECIMAL       );
public static final FixField NoDlvyInst                   = new FixField("NoDlvyInst"                ,   85, FieldType.GROUP_SIZE       );
public static final FixField AllocStatus                   = new FixField("AllocStatus"                ,   87, FieldType.INTEGER       );
public static final FixField AllocRejCode                   = new FixField("AllocRejCode"                ,   88, FieldType.INTEGER       );
public static final FixField Signature                   = new FixField("Signature"                ,   89, FieldType.STRING       );
public static final FixField SecureDataLen                   = new FixField("SecureDataLen"                ,   90, FieldType.INTEGER       );
public static final FixField SecureData                   = new FixField("SecureData"                ,   91, FieldType.STRING       );
public static final FixField SignatureLength                   = new FixField("SignatureLength"                ,   93, FieldType.INTEGER       );
public static final FixField EmailType                   = new FixField("EmailType"                ,   94, FieldType.CHAR       );
public static final FixField RawDataLength                   = new FixField("RawDataLength"                ,   95, FieldType.INTEGER       );
public static final FixField RawData                   = new FixField("RawData"                ,   96, FieldType.STRING       );
public static final FixField PossResend                   = new FixField("PossResend"                ,   97, FieldType.BOOLEAN       );
public static final FixField EncryptMethod                   = new FixField("EncryptMethod"                ,   98, FieldType.INTEGER       );
public static final FixField StopPx                   = new FixField("StopPx"                ,   99, FieldType.DECIMAL       );
public static final FixField ExDestination                   = new FixField("ExDestination"                ,   100, FieldType.STRING       );
public static final FixField CxlRejReason                   = new FixField("CxlRejReason"                ,   102, FieldType.INTEGER       );
public static final FixField OrdRejReason                   = new FixField("OrdRejReason"                ,   103, FieldType.INTEGER       );
public static final FixField IOIQualifier                   = new FixField("IOIQualifier"                ,   104, FieldType.CHAR       );
public static final FixField WaveNo                   = new FixField("WaveNo"                ,   105, FieldType.STRING       );
public static final FixField Issuer                   = new FixField("Issuer"                ,   106, FieldType.STRING       );
public static final FixField SecurityDesc                   = new FixField("SecurityDesc"                ,   107, FieldType.STRING       );
public static final FixField HeartBtInt                   = new FixField("HeartBtInt"                ,   108, FieldType.INTEGER       );
public static final FixField MinQty                   = new FixField("MinQty"                ,   110, FieldType.DECIMAL       );
public static final FixField MaxFloor                   = new FixField("MaxFloor"                ,   111, FieldType.DECIMAL       );
public static final FixField TestReqID                   = new FixField("TestReqID"                ,   112, FieldType.STRING       );
public static final FixField ReportToExch                   = new FixField("ReportToExch"                ,   113, FieldType.BOOLEAN       );
public static final FixField LocateReqd                   = new FixField("LocateReqd"                ,   114, FieldType.BOOLEAN       );
public static final FixField OnBehalfOfCompID                   = new FixField("OnBehalfOfCompID"                ,   115, FieldType.STRING       );
public static final FixField OnBehalfOfSubID                   = new FixField("OnBehalfOfSubID"                ,   116, FieldType.STRING       );
public static final FixField QuoteID                   = new FixField("QuoteID"                ,   117, FieldType.STRING       );
public static final FixField NetMoney                   = new FixField("NetMoney"                ,   118, FieldType.DECIMAL       );
public static final FixField SettlCurrAmt                   = new FixField("SettlCurrAmt"                ,   119, FieldType.DECIMAL       );
public static final FixField SettlCurrency                   = new FixField("SettlCurrency"                ,   120, FieldType.STRING       );
public static final FixField ForexReq                   = new FixField("ForexReq"                ,   121, FieldType.BOOLEAN       );
public static final FixField OrigSendingTime                   = new FixField("OrigSendingTime"                ,   122, FieldType.UTC_TIMESTAMP       );
public static final FixField GapFillFlag                   = new FixField("GapFillFlag"                ,   123, FieldType.BOOLEAN       );
public static final FixField NoExecs                   = new FixField("NoExecs"                ,   124, FieldType.GROUP_SIZE       );
public static final FixField ExpireTime                   = new FixField("ExpireTime"                ,   126, FieldType.UTC_TIMESTAMP       );
public static final FixField DKReason                   = new FixField("DKReason"                ,   127, FieldType.CHAR       );
public static final FixField DeliverToCompID                   = new FixField("DeliverToCompID"                ,   128, FieldType.STRING       );
public static final FixField DeliverToSubID                   = new FixField("DeliverToSubID"                ,   129, FieldType.STRING       );
public static final FixField IOINaturalFlag                   = new FixField("IOINaturalFlag"                ,   130, FieldType.BOOLEAN       );
public static final FixField QuoteReqID                   = new FixField("QuoteReqID"                ,   131, FieldType.STRING       );
public static final FixField BidPx                   = new FixField("BidPx"                ,   132, FieldType.DECIMAL       );
public static final FixField OfferPx                   = new FixField("OfferPx"                ,   133, FieldType.DECIMAL       );
public static final FixField BidSize                   = new FixField("BidSize"                ,   134, FieldType.DECIMAL       );
public static final FixField OfferSize                   = new FixField("OfferSize"                ,   135, FieldType.DECIMAL       );
public static final FixField NoMiscFees                   = new FixField("NoMiscFees"                ,   136, FieldType.GROUP_SIZE       );
public static final FixField MiscFeeAmt                   = new FixField("MiscFeeAmt"                ,   137, FieldType.DECIMAL       );
public static final FixField MiscFeeCurr                   = new FixField("MiscFeeCurr"                ,   138, FieldType.STRING       );
public static final FixField MiscFeeType                   = new FixField("MiscFeeType"                ,   139, FieldType.CHAR       );
public static final FixField PrevClosePx                   = new FixField("PrevClosePx"                ,   140, FieldType.DECIMAL       );
public static final FixField ResetSeqNumFlag                   = new FixField("ResetSeqNumFlag"                ,   141, FieldType.BOOLEAN       );
public static final FixField SenderLocationID                   = new FixField("SenderLocationID"                ,   142, FieldType.STRING       );
public static final FixField TargetLocationID                   = new FixField("TargetLocationID"                ,   143, FieldType.STRING       );
public static final FixField OnBehalfOfLocationID                   = new FixField("OnBehalfOfLocationID"                ,   144, FieldType.STRING       );
public static final FixField DeliverToLocationID                   = new FixField("DeliverToLocationID"                ,   145, FieldType.STRING       );
public static final FixField NoRelatedSym                   = new FixField("NoRelatedSym"                ,   146, FieldType.GROUP_SIZE       );
public static final FixField Subject                   = new FixField("Subject"                ,   147, FieldType.STRING       );
public static final FixField Headline                   = new FixField("Headline"                ,   148, FieldType.STRING       );
public static final FixField URLLink                   = new FixField("URLLink"                ,   149, FieldType.STRING       );
public static final FixField ExecType                   = new FixField("ExecType"                ,   150, FieldType.CHAR       );
public static final FixField LeavesQty                   = new FixField("LeavesQty"                ,   151, FieldType.DECIMAL       );
public static final FixField CashOrderQty                   = new FixField("CashOrderQty"                ,   152, FieldType.DECIMAL       );
public static final FixField AllocAvgPx                   = new FixField("AllocAvgPx"                ,   153, FieldType.DECIMAL       );
public static final FixField AllocNetMoney                   = new FixField("AllocNetMoney"                ,   154, FieldType.DECIMAL       );
public static final FixField SettlCurrFxRate                   = new FixField("SettlCurrFxRate"                ,   155, FieldType.DECIMAL       );
public static final FixField SettlCurrFxRateCalc                   = new FixField("SettlCurrFxRateCalc"                ,   156, FieldType.CHAR       );
public static final FixField NumDaysInterest                   = new FixField("NumDaysInterest"                ,   157, FieldType.INTEGER       );
public static final FixField AccruedInterestRate                   = new FixField("AccruedInterestRate"                ,   158, FieldType.DECIMAL       );
public static final FixField AccruedInterestAmt                   = new FixField("AccruedInterestAmt"                ,   159, FieldType.DECIMAL       );
public static final FixField SettlInstMode                   = new FixField("SettlInstMode"                ,   160, FieldType.CHAR       );
public static final FixField AllocText                   = new FixField("AllocText"                ,   161, FieldType.STRING       );
public static final FixField SettlInstID                   = new FixField("SettlInstID"                ,   162, FieldType.STRING       );
public static final FixField SettlInstTransType                   = new FixField("SettlInstTransType"                ,   163, FieldType.CHAR       );
public static final FixField EmailThreadID                   = new FixField("EmailThreadID"                ,   164, FieldType.STRING       );
public static final FixField SettlInstSource                   = new FixField("SettlInstSource"                ,   165, FieldType.CHAR       );
public static final FixField SecurityType                   = new FixField("SecurityType"                ,   167, FieldType.STRING       );
public static final FixField EffectiveTime                   = new FixField("EffectiveTime"                ,   168, FieldType.UTC_TIMESTAMP       );
public static final FixField StandInstDbType                   = new FixField("StandInstDbType"                ,   169, FieldType.INTEGER       );
public static final FixField StandInstDbName                   = new FixField("StandInstDbName"                ,   170, FieldType.STRING       );
public static final FixField StandInstDbID                   = new FixField("StandInstDbID"                ,   171, FieldType.STRING       );
public static final FixField SettlDeliveryType                   = new FixField("SettlDeliveryType"                ,   172, FieldType.INTEGER       );
public static final FixField BidSpotRate                   = new FixField("BidSpotRate"                ,   188, FieldType.DECIMAL       );
public static final FixField BidForwardPoints                   = new FixField("BidForwardPoints"                ,   189, FieldType.DECIMAL       );
public static final FixField OfferSpotRate                   = new FixField("OfferSpotRate"                ,   190, FieldType.DECIMAL       );
public static final FixField OfferForwardPoints                   = new FixField("OfferForwardPoints"                ,   191, FieldType.DECIMAL       );
public static final FixField OrderQty2                   = new FixField("OrderQty2"                ,   192, FieldType.DECIMAL       );
public static final FixField SettlDate2                   = new FixField("SettlDate2"                ,   193, FieldType.UTC_TIMESTAMP       );
public static final FixField LastSpotRate                   = new FixField("LastSpotRate"                ,   194, FieldType.DECIMAL       );
public static final FixField LastForwardPoints                   = new FixField("LastForwardPoints"                ,   195, FieldType.DECIMAL       );
public static final FixField AllocLinkID                   = new FixField("AllocLinkID"                ,   196, FieldType.STRING       );
public static final FixField AllocLinkType                   = new FixField("AllocLinkType"                ,   197, FieldType.INTEGER       );
public static final FixField SecondaryOrderID                   = new FixField("SecondaryOrderID"                ,   198, FieldType.STRING       );
public static final FixField NoIOIQualifiers                   = new FixField("NoIOIQualifiers"                ,   199, FieldType.GROUP_SIZE       );
public static final FixField MaturityMonthYear                   = new FixField("MaturityMonthYear"                ,   200, FieldType.STRING       );
public static final FixField PutOrCall                   = new FixField("PutOrCall"                ,   201, FieldType.INTEGER       );
public static final FixField StrikePrice                   = new FixField("StrikePrice"                ,   202, FieldType.DECIMAL       );
public static final FixField CoveredOrUncovered                   = new FixField("CoveredOrUncovered"                ,   203, FieldType.INTEGER       );
public static final FixField OptAttribute                   = new FixField("OptAttribute"                ,   206, FieldType.CHAR       );
public static final FixField SecurityExchange                   = new FixField("SecurityExchange"                ,   207, FieldType.STRING       );
public static final FixField NotifyBrokerOfCredit                   = new FixField("NotifyBrokerOfCredit"                ,   208, FieldType.BOOLEAN       );
public static final FixField AllocHandlInst                   = new FixField("AllocHandlInst"                ,   209, FieldType.INTEGER       );
public static final FixField MaxShow                   = new FixField("MaxShow"                ,   210, FieldType.DECIMAL       );
public static final FixField PegOffsetValue                   = new FixField("PegOffsetValue"                ,   211, FieldType.DECIMAL       );
public static final FixField XmlDataLen                   = new FixField("XmlDataLen"                ,   212, FieldType.INTEGER       );
public static final FixField XmlData                   = new FixField("XmlData"                ,   213, FieldType.STRING       );
public static final FixField SettlInstRefID                   = new FixField("SettlInstRefID"                ,   214, FieldType.STRING       );
public static final FixField NoRoutingIDs                   = new FixField("NoRoutingIDs"                ,   215, FieldType.GROUP_SIZE       );
public static final FixField RoutingType                   = new FixField("RoutingType"                ,   216, FieldType.INTEGER       );
public static final FixField RoutingID                   = new FixField("RoutingID"                ,   217, FieldType.STRING       );
public static final FixField Spread                   = new FixField("Spread"                ,   218, FieldType.DECIMAL       );
public static final FixField BenchmarkCurveCurrency                   = new FixField("BenchmarkCurveCurrency"                ,   220, FieldType.STRING       );
public static final FixField BenchmarkCurveName                   = new FixField("BenchmarkCurveName"                ,   221, FieldType.STRING       );
public static final FixField BenchmarkCurvePoint                   = new FixField("BenchmarkCurvePoint"                ,   222, FieldType.STRING       );
public static final FixField CouponRate                   = new FixField("CouponRate"                ,   223, FieldType.DECIMAL       );
public static final FixField CouponPaymentDate                   = new FixField("CouponPaymentDate"                ,   224, FieldType.UTC_TIMESTAMP       );
public static final FixField IssueDate                   = new FixField("IssueDate"                ,   225, FieldType.UTC_TIMESTAMP       );
public static final FixField RepurchaseTerm                   = new FixField("RepurchaseTerm"                ,   226, FieldType.INTEGER       );
public static final FixField RepurchaseRate                   = new FixField("RepurchaseRate"                ,   227, FieldType.DECIMAL       );
public static final FixField Factor                   = new FixField("Factor"                ,   228, FieldType.DECIMAL       );
public static final FixField TradeOriginationDate                   = new FixField("TradeOriginationDate"                ,   229, FieldType.UTC_TIMESTAMP       );
public static final FixField ExDate                   = new FixField("ExDate"                ,   230, FieldType.UTC_TIMESTAMP       );
public static final FixField ContractMultiplier                   = new FixField("ContractMultiplier"                ,   231, FieldType.DECIMAL       );
public static final FixField NoStipulations                   = new FixField("NoStipulations"                ,   232, FieldType.GROUP_SIZE       );
public static final FixField StipulationType                   = new FixField("StipulationType"                ,   233, FieldType.STRING       );
public static final FixField StipulationValue                   = new FixField("StipulationValue"                ,   234, FieldType.STRING       );
public static final FixField YieldType                   = new FixField("YieldType"                ,   235, FieldType.STRING       );
public static final FixField Yield                   = new FixField("Yield"                ,   236, FieldType.DECIMAL       );
public static final FixField TotalTakedown                   = new FixField("TotalTakedown"                ,   237, FieldType.DECIMAL       );
public static final FixField Concession                   = new FixField("Concession"                ,   238, FieldType.DECIMAL       );
public static final FixField RepoCollateralSecurityType                   = new FixField("RepoCollateralSecurityType"                ,   239, FieldType.STRING       );
public static final FixField RedemptionDate                   = new FixField("RedemptionDate"                ,   240, FieldType.UTC_TIMESTAMP       );
public static final FixField UnderlyingCouponPaymentDate                   = new FixField("UnderlyingCouponPaymentDate"                ,   241, FieldType.UTC_TIMESTAMP       );
public static final FixField UnderlyingIssueDate                   = new FixField("UnderlyingIssueDate"                ,   242, FieldType.UTC_TIMESTAMP       );
public static final FixField UnderlyingRepoCollateralSecurityType                   = new FixField("UnderlyingRepoCollateralSecurityType"                ,   243, FieldType.STRING       );
public static final FixField UnderlyingRepurchaseTerm                   = new FixField("UnderlyingRepurchaseTerm"                ,   244, FieldType.INTEGER       );
public static final FixField UnderlyingRepurchaseRate                   = new FixField("UnderlyingRepurchaseRate"                ,   245, FieldType.DECIMAL       );
public static final FixField UnderlyingFactor                   = new FixField("UnderlyingFactor"                ,   246, FieldType.DECIMAL       );
public static final FixField UnderlyingRedemptionDate                   = new FixField("UnderlyingRedemptionDate"                ,   247, FieldType.UTC_TIMESTAMP       );
public static final FixField LegCouponPaymentDate                   = new FixField("LegCouponPaymentDate"                ,   248, FieldType.UTC_TIMESTAMP       );
public static final FixField LegIssueDate                   = new FixField("LegIssueDate"                ,   249, FieldType.UTC_TIMESTAMP       );
public static final FixField LegRepoCollateralSecurityType                   = new FixField("LegRepoCollateralSecurityType"                ,   250, FieldType.STRING       );
public static final FixField LegRepurchaseTerm                   = new FixField("LegRepurchaseTerm"                ,   251, FieldType.INTEGER       );
public static final FixField LegRepurchaseRate                   = new FixField("LegRepurchaseRate"                ,   252, FieldType.DECIMAL       );
public static final FixField LegFactor                   = new FixField("LegFactor"                ,   253, FieldType.DECIMAL       );
public static final FixField LegRedemptionDate                   = new FixField("LegRedemptionDate"                ,   254, FieldType.UTC_TIMESTAMP       );
public static final FixField CreditRating                   = new FixField("CreditRating"                ,   255, FieldType.STRING       );
public static final FixField UnderlyingCreditRating                   = new FixField("UnderlyingCreditRating"                ,   256, FieldType.STRING       );
public static final FixField LegCreditRating                   = new FixField("LegCreditRating"                ,   257, FieldType.STRING       );
public static final FixField TradedFlatSwitch                   = new FixField("TradedFlatSwitch"                ,   258, FieldType.BOOLEAN       );
public static final FixField BasisFeatureDate                   = new FixField("BasisFeatureDate"                ,   259, FieldType.UTC_TIMESTAMP       );
public static final FixField BasisFeaturePrice                   = new FixField("BasisFeaturePrice"                ,   260, FieldType.DECIMAL       );
public static final FixField MDReqID                   = new FixField("MDReqID"                ,   262, FieldType.STRING       );
public static final FixField SubscriptionRequestType                   = new FixField("SubscriptionRequestType"                ,   263, FieldType.CHAR       );
public static final FixField MarketDepth                   = new FixField("MarketDepth"                ,   264, FieldType.INTEGER       );
public static final FixField MDUpdateType                   = new FixField("MDUpdateType"                ,   265, FieldType.INTEGER       );
public static final FixField AggregatedBook                   = new FixField("AggregatedBook"                ,   266, FieldType.BOOLEAN       );
public static final FixField NoMDEntryTypes                   = new FixField("NoMDEntryTypes"                ,   267, FieldType.GROUP_SIZE       );
public static final FixField NoMDEntries                   = new FixField("NoMDEntries"                ,   268, FieldType.GROUP_SIZE       );
public static final FixField MDEntryType                   = new FixField("MDEntryType"                ,   269, FieldType.CHAR       );
public static final FixField MDEntryPx                   = new FixField("MDEntryPx"                ,   270, FieldType.DECIMAL       );
public static final FixField MDEntrySize                   = new FixField("MDEntrySize"                ,   271, FieldType.DECIMAL       );
public static final FixField MDEntryDate                   = new FixField("MDEntryDate"                ,   272, FieldType.UTC_TIMESTAMP       );
public static final FixField MDEntryTime                   = new FixField("MDEntryTime"                ,   273, FieldType.UTC_TIMESTAMP       );
public static final FixField TickDirection                   = new FixField("TickDirection"                ,   274, FieldType.CHAR       );
public static final FixField MDMkt                   = new FixField("MDMkt"                ,   275, FieldType.STRING       );
public static final FixField QuoteCondition                   = new FixField("QuoteCondition"                ,   276, FieldType.STRING       );
public static final FixField TradeCondition                   = new FixField("TradeCondition"                ,   277, FieldType.STRING       );
public static final FixField MDEntryID                   = new FixField("MDEntryID"                ,   278, FieldType.STRING       );
public static final FixField MDUpdateAction                   = new FixField("MDUpdateAction"                ,   279, FieldType.CHAR       );
public static final FixField MDEntryRefID                   = new FixField("MDEntryRefID"                ,   280, FieldType.STRING       );
public static final FixField MDReqRejReason                   = new FixField("MDReqRejReason"                ,   281, FieldType.CHAR       );
public static final FixField MDEntryOriginator                   = new FixField("MDEntryOriginator"                ,   282, FieldType.STRING       );
public static final FixField LocationID                   = new FixField("LocationID"                ,   283, FieldType.STRING       );
public static final FixField DeskID                   = new FixField("DeskID"                ,   284, FieldType.STRING       );
public static final FixField DeleteReason                   = new FixField("DeleteReason"                ,   285, FieldType.CHAR       );
public static final FixField OpenCloseSettlFlag                   = new FixField("OpenCloseSettlFlag"                ,   286, FieldType.STRING       );
public static final FixField SellerDays                   = new FixField("SellerDays"                ,   287, FieldType.INTEGER       );
public static final FixField MDEntryBuyer                   = new FixField("MDEntryBuyer"                ,   288, FieldType.STRING       );
public static final FixField MDEntrySeller                   = new FixField("MDEntrySeller"                ,   289, FieldType.STRING       );
public static final FixField MDEntryPositionNo                   = new FixField("MDEntryPositionNo"                ,   290, FieldType.INTEGER       );
public static final FixField FinancialStatus                   = new FixField("FinancialStatus"                ,   291, FieldType.STRING       );
public static final FixField CorporateAction                   = new FixField("CorporateAction"                ,   292, FieldType.STRING       );
public static final FixField DefBidSize                   = new FixField("DefBidSize"                ,   293, FieldType.DECIMAL       );
public static final FixField DefOfferSize                   = new FixField("DefOfferSize"                ,   294, FieldType.DECIMAL       );
public static final FixField NoQuoteEntries                   = new FixField("NoQuoteEntries"                ,   295, FieldType.GROUP_SIZE       );
public static final FixField NoQuoteSets                   = new FixField("NoQuoteSets"                ,   296, FieldType.GROUP_SIZE       );
public static final FixField QuoteStatus                   = new FixField("QuoteStatus"                ,   297, FieldType.INTEGER       );
public static final FixField QuoteCancelType                   = new FixField("QuoteCancelType"                ,   298, FieldType.INTEGER       );
public static final FixField QuoteEntryID                   = new FixField("QuoteEntryID"                ,   299, FieldType.STRING       );
public static final FixField QuoteRejectReason                   = new FixField("QuoteRejectReason"                ,   300, FieldType.INTEGER       );
public static final FixField QuoteResponseLevel                   = new FixField("QuoteResponseLevel"                ,   301, FieldType.INTEGER       );
public static final FixField QuoteSetID                   = new FixField("QuoteSetID"                ,   302, FieldType.STRING       );
public static final FixField QuoteRequestType                   = new FixField("QuoteRequestType"                ,   303, FieldType.INTEGER       );
public static final FixField TotNoQuoteEntries                   = new FixField("TotNoQuoteEntries"                ,   304, FieldType.INTEGER       );
public static final FixField UnderlyingSecurityIDSource                   = new FixField("UnderlyingSecurityIDSource"                ,   305, FieldType.STRING       );
public static final FixField UnderlyingIssuer                   = new FixField("UnderlyingIssuer"                ,   306, FieldType.STRING       );
public static final FixField UnderlyingSecurityDesc                   = new FixField("UnderlyingSecurityDesc"                ,   307, FieldType.STRING       );
public static final FixField UnderlyingSecurityExchange                   = new FixField("UnderlyingSecurityExchange"                ,   308, FieldType.STRING       );
public static final FixField UnderlyingSecurityID                   = new FixField("UnderlyingSecurityID"                ,   309, FieldType.STRING       );
public static final FixField UnderlyingSecurityType                   = new FixField("UnderlyingSecurityType"                ,   310, FieldType.STRING       );
public static final FixField UnderlyingSymbol                   = new FixField("UnderlyingSymbol"                ,   311, FieldType.STRING       );
public static final FixField UnderlyingSymbolSfx                   = new FixField("UnderlyingSymbolSfx"                ,   312, FieldType.STRING       );
public static final FixField UnderlyingMaturityMonthYear                   = new FixField("UnderlyingMaturityMonthYear"                ,   313, FieldType.STRING       );
public static final FixField UnderlyingPutOrCall                   = new FixField("UnderlyingPutOrCall"                ,   315, FieldType.INTEGER       );
public static final FixField UnderlyingStrikePrice                   = new FixField("UnderlyingStrikePrice"                ,   316, FieldType.DECIMAL       );
public static final FixField UnderlyingOptAttribute                   = new FixField("UnderlyingOptAttribute"                ,   317, FieldType.CHAR       );
public static final FixField UnderlyingCurrency                   = new FixField("UnderlyingCurrency"                ,   318, FieldType.STRING       );
public static final FixField SecurityReqID                   = new FixField("SecurityReqID"                ,   320, FieldType.STRING       );
public static final FixField SecurityRequestType                   = new FixField("SecurityRequestType"                ,   321, FieldType.INTEGER       );
public static final FixField SecurityResponseID                   = new FixField("SecurityResponseID"                ,   322, FieldType.STRING       );
public static final FixField SecurityResponseType                   = new FixField("SecurityResponseType"                ,   323, FieldType.INTEGER       );
public static final FixField SecurityStatusReqID                   = new FixField("SecurityStatusReqID"                ,   324, FieldType.STRING       );
public static final FixField UnsolicitedIndicator                   = new FixField("UnsolicitedIndicator"                ,   325, FieldType.BOOLEAN       );
public static final FixField SecurityTradingStatus                   = new FixField("SecurityTradingStatus"                ,   326, FieldType.INTEGER       );
public static final FixField HaltReason                   = new FixField("HaltReason"                ,   327, FieldType.CHAR       );
public static final FixField InViewOfCommon                   = new FixField("InViewOfCommon"                ,   328, FieldType.BOOLEAN       );
public static final FixField DueToRelated                   = new FixField("DueToRelated"                ,   329, FieldType.BOOLEAN       );
public static final FixField BuyVolume                   = new FixField("BuyVolume"                ,   330, FieldType.DECIMAL       );
public static final FixField SellVolume                   = new FixField("SellVolume"                ,   331, FieldType.DECIMAL       );
public static final FixField HighPx                   = new FixField("HighPx"                ,   332, FieldType.DECIMAL       );
public static final FixField LowPx                   = new FixField("LowPx"                ,   333, FieldType.DECIMAL       );
public static final FixField Adjustment                   = new FixField("Adjustment"                ,   334, FieldType.INTEGER       );
public static final FixField TradSesReqID                   = new FixField("TradSesReqID"                ,   335, FieldType.STRING       );
public static final FixField TradingSessionID                   = new FixField("TradingSessionID"                ,   336, FieldType.STRING       );
public static final FixField ContraTrader                   = new FixField("ContraTrader"                ,   337, FieldType.STRING       );
public static final FixField TradSesMethod                   = new FixField("TradSesMethod"                ,   338, FieldType.INTEGER       );
public static final FixField TradSesMode                   = new FixField("TradSesMode"                ,   339, FieldType.INTEGER       );
public static final FixField TradSesStatus                   = new FixField("TradSesStatus"                ,   340, FieldType.INTEGER       );
public static final FixField TradSesStartTime                   = new FixField("TradSesStartTime"                ,   341, FieldType.UTC_TIMESTAMP       );
public static final FixField TradSesOpenTime                   = new FixField("TradSesOpenTime"                ,   342, FieldType.UTC_TIMESTAMP       );
public static final FixField TradSesPreCloseTime                   = new FixField("TradSesPreCloseTime"                ,   343, FieldType.UTC_TIMESTAMP       );
public static final FixField TradSesCloseTime                   = new FixField("TradSesCloseTime"                ,   344, FieldType.UTC_TIMESTAMP       );
public static final FixField TradSesEndTime                   = new FixField("TradSesEndTime"                ,   345, FieldType.UTC_TIMESTAMP       );
public static final FixField NumberOfOrders                   = new FixField("NumberOfOrders"                ,   346, FieldType.INTEGER       );
public static final FixField MessageEncoding                   = new FixField("MessageEncoding"                ,   347, FieldType.STRING       );
public static final FixField EncodedIssuerLen                   = new FixField("EncodedIssuerLen"                ,   348, FieldType.INTEGER       );
public static final FixField EncodedIssuer                   = new FixField("EncodedIssuer"                ,   349, FieldType.STRING       );
public static final FixField EncodedSecurityDescLen                   = new FixField("EncodedSecurityDescLen"                ,   350, FieldType.INTEGER       );
public static final FixField EncodedSecurityDesc                   = new FixField("EncodedSecurityDesc"                ,   351, FieldType.STRING       );
public static final FixField EncodedListExecInstLen                   = new FixField("EncodedListExecInstLen"                ,   352, FieldType.INTEGER       );
public static final FixField EncodedListExecInst                   = new FixField("EncodedListExecInst"                ,   353, FieldType.STRING       );
public static final FixField EncodedTextLen                   = new FixField("EncodedTextLen"                ,   354, FieldType.INTEGER       );
public static final FixField EncodedText                   = new FixField("EncodedText"                ,   355, FieldType.STRING       );
public static final FixField EncodedSubjectLen                   = new FixField("EncodedSubjectLen"                ,   356, FieldType.INTEGER       );
public static final FixField EncodedSubject                   = new FixField("EncodedSubject"                ,   357, FieldType.STRING       );
public static final FixField EncodedHeadlineLen                   = new FixField("EncodedHeadlineLen"                ,   358, FieldType.INTEGER       );
public static final FixField EncodedHeadline                   = new FixField("EncodedHeadline"                ,   359, FieldType.STRING       );
public static final FixField EncodedAllocTextLen                   = new FixField("EncodedAllocTextLen"                ,   360, FieldType.INTEGER       );
public static final FixField EncodedAllocText                   = new FixField("EncodedAllocText"                ,   361, FieldType.STRING       );
public static final FixField EncodedUnderlyingIssuerLen                   = new FixField("EncodedUnderlyingIssuerLen"                ,   362, FieldType.INTEGER       );
public static final FixField EncodedUnderlyingIssuer                   = new FixField("EncodedUnderlyingIssuer"                ,   363, FieldType.STRING       );
public static final FixField EncodedUnderlyingSecurityDescLen                   = new FixField("EncodedUnderlyingSecurityDescLen"                ,   364, FieldType.INTEGER       );
public static final FixField EncodedUnderlyingSecurityDesc                   = new FixField("EncodedUnderlyingSecurityDesc"                ,   365, FieldType.STRING       );
public static final FixField AllocPrice                   = new FixField("AllocPrice"                ,   366, FieldType.DECIMAL       );
public static final FixField QuoteSetValidUntilTime                   = new FixField("QuoteSetValidUntilTime"                ,   367, FieldType.UTC_TIMESTAMP       );
public static final FixField QuoteEntryRejectReason                   = new FixField("QuoteEntryRejectReason"                ,   368, FieldType.INTEGER       );
public static final FixField LastMsgSeqNumProcessed                   = new FixField("LastMsgSeqNumProcessed"                ,   369, FieldType.INTEGER       );
public static final FixField RefTagID                   = new FixField("RefTagID"                ,   371, FieldType.INTEGER       );
public static final FixField RefMsgType                   = new FixField("RefMsgType"                ,   372, FieldType.STRING       );
public static final FixField SessionRejectReason                   = new FixField("SessionRejectReason"                ,   373, FieldType.INTEGER       );
public static final FixField BidRequestTransType                   = new FixField("BidRequestTransType"                ,   374, FieldType.CHAR       );
public static final FixField ContraBroker                   = new FixField("ContraBroker"                ,   375, FieldType.STRING       );
public static final FixField ComplianceID                   = new FixField("ComplianceID"                ,   376, FieldType.STRING       );
public static final FixField SolicitedFlag                   = new FixField("SolicitedFlag"                ,   377, FieldType.BOOLEAN       );
public static final FixField ExecRestatementReason                   = new FixField("ExecRestatementReason"                ,   378, FieldType.INTEGER       );
public static final FixField BusinessRejectRefID                   = new FixField("BusinessRejectRefID"                ,   379, FieldType.STRING       );
public static final FixField BusinessRejectReason                   = new FixField("BusinessRejectReason"                ,   380, FieldType.INTEGER       );
public static final FixField GrossTradeAmt                   = new FixField("GrossTradeAmt"                ,   381, FieldType.DECIMAL       );
public static final FixField NoContraBrokers                   = new FixField("NoContraBrokers"                ,   382, FieldType.GROUP_SIZE       );
public static final FixField MaxMessageSize                   = new FixField("MaxMessageSize"                ,   383, FieldType.INTEGER       );
public static final FixField NoMsgTypes                   = new FixField("NoMsgTypes"                ,   384, FieldType.GROUP_SIZE       );
public static final FixField MsgDirection                   = new FixField("MsgDirection"                ,   385, FieldType.CHAR       );
public static final FixField NoTradingSessions                   = new FixField("NoTradingSessions"                ,   386, FieldType.GROUP_SIZE       );
public static final FixField TotalVolumeTraded                   = new FixField("TotalVolumeTraded"                ,   387, FieldType.DECIMAL       );
public static final FixField DiscretionInst                   = new FixField("DiscretionInst"                ,   388, FieldType.CHAR       );
public static final FixField DiscretionOffsetValue                   = new FixField("DiscretionOffsetValue"                ,   389, FieldType.DECIMAL       );
public static final FixField BidID                   = new FixField("BidID"                ,   390, FieldType.STRING       );
public static final FixField ClientBidID                   = new FixField("ClientBidID"                ,   391, FieldType.STRING       );
public static final FixField ListName                   = new FixField("ListName"                ,   392, FieldType.STRING       );
public static final FixField TotNoRelatedSym                   = new FixField("TotNoRelatedSym"                ,   393, FieldType.INTEGER       );
public static final FixField BidType                   = new FixField("BidType"                ,   394, FieldType.INTEGER       );
public static final FixField NumTickets                   = new FixField("NumTickets"                ,   395, FieldType.INTEGER       );
public static final FixField SideValue1                   = new FixField("SideValue1"                ,   396, FieldType.DECIMAL       );
public static final FixField SideValue2                   = new FixField("SideValue2"                ,   397, FieldType.DECIMAL       );
public static final FixField NoBidDescriptors                   = new FixField("NoBidDescriptors"                ,   398, FieldType.GROUP_SIZE       );
public static final FixField BidDescriptorType                   = new FixField("BidDescriptorType"                ,   399, FieldType.INTEGER       );
public static final FixField BidDescriptor                   = new FixField("BidDescriptor"                ,   400, FieldType.STRING       );
public static final FixField SideValueInd                   = new FixField("SideValueInd"                ,   401, FieldType.INTEGER       );
public static final FixField LiquidityPctLow                   = new FixField("LiquidityPctLow"                ,   402, FieldType.DECIMAL       );
public static final FixField LiquidityPctHigh                   = new FixField("LiquidityPctHigh"                ,   403, FieldType.DECIMAL       );
public static final FixField LiquidityValue                   = new FixField("LiquidityValue"                ,   404, FieldType.DECIMAL       );
public static final FixField EFPTrackingError                   = new FixField("EFPTrackingError"                ,   405, FieldType.DECIMAL       );
public static final FixField FairValue                   = new FixField("FairValue"                ,   406, FieldType.DECIMAL       );
public static final FixField OutsideIndexPct                   = new FixField("OutsideIndexPct"                ,   407, FieldType.DECIMAL       );
public static final FixField ValueOfFutures                   = new FixField("ValueOfFutures"                ,   408, FieldType.DECIMAL       );
public static final FixField LiquidityIndType                   = new FixField("LiquidityIndType"                ,   409, FieldType.INTEGER       );
public static final FixField WtAverageLiquidity                   = new FixField("WtAverageLiquidity"                ,   410, FieldType.DECIMAL       );
public static final FixField ExchangeForPhysical                   = new FixField("ExchangeForPhysical"                ,   411, FieldType.BOOLEAN       );
public static final FixField OutMainCntryUIndex                   = new FixField("OutMainCntryUIndex"                ,   412, FieldType.DECIMAL       );
public static final FixField CrossPercent                   = new FixField("CrossPercent"                ,   413, FieldType.DECIMAL       );
public static final FixField ProgRptReqs                   = new FixField("ProgRptReqs"                ,   414, FieldType.INTEGER       );
public static final FixField ProgPeriodInterval                   = new FixField("ProgPeriodInterval"                ,   415, FieldType.INTEGER       );
public static final FixField IncTaxInd                   = new FixField("IncTaxInd"                ,   416, FieldType.INTEGER       );
public static final FixField NumBidders                   = new FixField("NumBidders"                ,   417, FieldType.INTEGER       );
public static final FixField BidTradeType                   = new FixField("BidTradeType"                ,   418, FieldType.CHAR       );
public static final FixField BasisPxType                   = new FixField("BasisPxType"                ,   419, FieldType.CHAR       );
public static final FixField NoBidComponents                   = new FixField("NoBidComponents"                ,   420, FieldType.GROUP_SIZE       );
public static final FixField Country                   = new FixField("Country"                ,   421, FieldType.STRING       );
public static final FixField TotNoStrikes                   = new FixField("TotNoStrikes"                ,   422, FieldType.INTEGER       );
public static final FixField PriceType                   = new FixField("PriceType"                ,   423, FieldType.INTEGER       );
public static final FixField DayOrderQty                   = new FixField("DayOrderQty"                ,   424, FieldType.DECIMAL       );
public static final FixField DayCumQty                   = new FixField("DayCumQty"                ,   425, FieldType.DECIMAL       );
public static final FixField DayAvgPx                   = new FixField("DayAvgPx"                ,   426, FieldType.DECIMAL       );
public static final FixField GTBookingInst                   = new FixField("GTBookingInst"                ,   427, FieldType.INTEGER       );
public static final FixField NoStrikes                   = new FixField("NoStrikes"                ,   428, FieldType.GROUP_SIZE       );
public static final FixField ListStatusType                   = new FixField("ListStatusType"                ,   429, FieldType.INTEGER       );
public static final FixField NetGrossInd                   = new FixField("NetGrossInd"                ,   430, FieldType.INTEGER       );
public static final FixField ListOrderStatus                   = new FixField("ListOrderStatus"                ,   431, FieldType.INTEGER       );
public static final FixField ExpireDate                   = new FixField("ExpireDate"                ,   432, FieldType.UTC_TIMESTAMP       );
public static final FixField ListExecInstType                   = new FixField("ListExecInstType"                ,   433, FieldType.CHAR       );
public static final FixField CxlRejResponseTo                   = new FixField("CxlRejResponseTo"                ,   434, FieldType.CHAR       );
public static final FixField UnderlyingCouponRate                   = new FixField("UnderlyingCouponRate"                ,   435, FieldType.DECIMAL       );
public static final FixField UnderlyingContractMultiplier                   = new FixField("UnderlyingContractMultiplier"                ,   436, FieldType.DECIMAL       );
public static final FixField ContraTradeQty                   = new FixField("ContraTradeQty"                ,   437, FieldType.DECIMAL       );
public static final FixField ContraTradeTime                   = new FixField("ContraTradeTime"                ,   438, FieldType.UTC_TIMESTAMP       );
public static final FixField LiquidityNumSecurities                   = new FixField("LiquidityNumSecurities"                ,   441, FieldType.INTEGER       );
public static final FixField MultiLegReportingType                   = new FixField("MultiLegReportingType"                ,   442, FieldType.CHAR       );
public static final FixField StrikeTime                   = new FixField("StrikeTime"                ,   443, FieldType.UTC_TIMESTAMP       );
public static final FixField ListStatusText                   = new FixField("ListStatusText"                ,   444, FieldType.STRING       );
public static final FixField EncodedListStatusTextLen                   = new FixField("EncodedListStatusTextLen"                ,   445, FieldType.INTEGER       );
public static final FixField EncodedListStatusText                   = new FixField("EncodedListStatusText"                ,   446, FieldType.STRING       );
public static final FixField PartyIDSource                   = new FixField("PartyIDSource"                ,   447, FieldType.CHAR       );
public static final FixField PartyID                   = new FixField("PartyID"                ,   448, FieldType.STRING       );
public static final FixField NetChgPrevDay                   = new FixField("NetChgPrevDay"                ,   451, FieldType.DECIMAL       );
public static final FixField PartyRole                   = new FixField("PartyRole"                ,   452, FieldType.INTEGER       );
public static final FixField NoPartyIDs                   = new FixField("NoPartyIDs"                ,   453, FieldType.GROUP_SIZE       );
public static final FixField NoSecurityAltID                   = new FixField("NoSecurityAltID"                ,   454, FieldType.GROUP_SIZE       );
public static final FixField SecurityAltID                   = new FixField("SecurityAltID"                ,   455, FieldType.STRING       );
public static final FixField SecurityAltIDSource                   = new FixField("SecurityAltIDSource"                ,   456, FieldType.STRING       );
public static final FixField NoUnderlyingSecurityAltID                   = new FixField("NoUnderlyingSecurityAltID"                ,   457, FieldType.GROUP_SIZE       );
public static final FixField UnderlyingSecurityAltID                   = new FixField("UnderlyingSecurityAltID"                ,   458, FieldType.STRING       );
public static final FixField UnderlyingSecurityAltIDSource                   = new FixField("UnderlyingSecurityAltIDSource"                ,   459, FieldType.STRING       );
public static final FixField Product                   = new FixField("Product"                ,   460, FieldType.INTEGER       );
public static final FixField CFICode                   = new FixField("CFICode"                ,   461, FieldType.STRING       );
public static final FixField UnderlyingProduct                   = new FixField("UnderlyingProduct"                ,   462, FieldType.INTEGER       );
public static final FixField UnderlyingCFICode                   = new FixField("UnderlyingCFICode"                ,   463, FieldType.STRING       );
public static final FixField TestMessageIndicator                   = new FixField("TestMessageIndicator"                ,   464, FieldType.BOOLEAN       );
public static final FixField QuantityType                   = new FixField("QuantityType"                ,   465, FieldType.INTEGER       );
public static final FixField BookingRefID                   = new FixField("BookingRefID"                ,   466, FieldType.STRING       );
public static final FixField IndividualAllocID                   = new FixField("IndividualAllocID"                ,   467, FieldType.STRING       );
public static final FixField RoundingDirection                   = new FixField("RoundingDirection"                ,   468, FieldType.CHAR       );
public static final FixField RoundingModulus                   = new FixField("RoundingModulus"                ,   469, FieldType.DECIMAL       );
public static final FixField CountryOfIssue                   = new FixField("CountryOfIssue"                ,   470, FieldType.STRING       );
public static final FixField StateOrProvinceOfIssue                   = new FixField("StateOrProvinceOfIssue"                ,   471, FieldType.STRING       );
public static final FixField LocaleOfIssue                   = new FixField("LocaleOfIssue"                ,   472, FieldType.STRING       );
public static final FixField NoRegistDtls                   = new FixField("NoRegistDtls"                ,   473, FieldType.GROUP_SIZE       );
public static final FixField MailingDtls                   = new FixField("MailingDtls"                ,   474, FieldType.STRING       );
public static final FixField InvestorCountryOfResidence                   = new FixField("InvestorCountryOfResidence"                ,   475, FieldType.STRING       );
public static final FixField PaymentRef                   = new FixField("PaymentRef"                ,   476, FieldType.STRING       );
public static final FixField DistribPaymentMethod                   = new FixField("DistribPaymentMethod"                ,   477, FieldType.INTEGER       );
public static final FixField CashDistribCurr                   = new FixField("CashDistribCurr"                ,   478, FieldType.STRING       );
public static final FixField CommCurrency                   = new FixField("CommCurrency"                ,   479, FieldType.STRING       );
public static final FixField CancellationRights                   = new FixField("CancellationRights"                ,   480, FieldType.CHAR       );
public static final FixField MoneyLaunderingStatus                   = new FixField("MoneyLaunderingStatus"                ,   481, FieldType.CHAR       );
public static final FixField MailingInst                   = new FixField("MailingInst"                ,   482, FieldType.STRING       );
public static final FixField TransBkdTime                   = new FixField("TransBkdTime"                ,   483, FieldType.UTC_TIMESTAMP       );
public static final FixField ExecPriceType                   = new FixField("ExecPriceType"                ,   484, FieldType.CHAR       );
public static final FixField ExecPriceAdjustment                   = new FixField("ExecPriceAdjustment"                ,   485, FieldType.DECIMAL       );
public static final FixField DateOfBirth                   = new FixField("DateOfBirth"                ,   486, FieldType.UTC_TIMESTAMP       );
public static final FixField TradeReportTransType                   = new FixField("TradeReportTransType"                ,   487, FieldType.INTEGER       );
public static final FixField CardHolderName                   = new FixField("CardHolderName"                ,   488, FieldType.STRING       );
public static final FixField CardNumber                   = new FixField("CardNumber"                ,   489, FieldType.STRING       );
public static final FixField CardExpDate                   = new FixField("CardExpDate"                ,   490, FieldType.UTC_TIMESTAMP       );
public static final FixField CardIssNum                   = new FixField("CardIssNum"                ,   491, FieldType.STRING       );
public static final FixField PaymentMethod                   = new FixField("PaymentMethod"                ,   492, FieldType.INTEGER       );
public static final FixField RegistAcctType                   = new FixField("RegistAcctType"                ,   493, FieldType.STRING       );
public static final FixField Designation                   = new FixField("Designation"                ,   494, FieldType.STRING       );
public static final FixField TaxAdvantageType                   = new FixField("TaxAdvantageType"                ,   495, FieldType.INTEGER       );
public static final FixField RegistRejReasonText                   = new FixField("RegistRejReasonText"                ,   496, FieldType.STRING       );
public static final FixField FundRenewWaiv                   = new FixField("FundRenewWaiv"                ,   497, FieldType.CHAR       );
public static final FixField CashDistribAgentName                   = new FixField("CashDistribAgentName"                ,   498, FieldType.STRING       );
public static final FixField CashDistribAgentCode                   = new FixField("CashDistribAgentCode"                ,   499, FieldType.STRING       );
public static final FixField CashDistribAgentAcctNumber                   = new FixField("CashDistribAgentAcctNumber"                ,   500, FieldType.STRING       );
public static final FixField CashDistribPayRef                   = new FixField("CashDistribPayRef"                ,   501, FieldType.STRING       );
public static final FixField CashDistribAgentAcctName                   = new FixField("CashDistribAgentAcctName"                ,   502, FieldType.STRING       );
public static final FixField CardStartDate                   = new FixField("CardStartDate"                ,   503, FieldType.UTC_TIMESTAMP       );
public static final FixField PaymentDate                   = new FixField("PaymentDate"                ,   504, FieldType.UTC_TIMESTAMP       );
public static final FixField PaymentRemitterID                   = new FixField("PaymentRemitterID"                ,   505, FieldType.STRING       );
public static final FixField RegistStatus                   = new FixField("RegistStatus"                ,   506, FieldType.CHAR       );
public static final FixField RegistRejReasonCode                   = new FixField("RegistRejReasonCode"                ,   507, FieldType.INTEGER       );
public static final FixField RegistRefID                   = new FixField("RegistRefID"                ,   508, FieldType.STRING       );
public static final FixField RegistDtls                   = new FixField("RegistDtls"                ,   509, FieldType.STRING       );
public static final FixField NoDistribInsts                   = new FixField("NoDistribInsts"                ,   510, FieldType.GROUP_SIZE       );
public static final FixField RegistEmail                   = new FixField("RegistEmail"                ,   511, FieldType.STRING       );
public static final FixField DistribPercentage                   = new FixField("DistribPercentage"                ,   512, FieldType.DECIMAL       );
public static final FixField RegistID                   = new FixField("RegistID"                ,   513, FieldType.STRING       );
public static final FixField RegistTransType                   = new FixField("RegistTransType"                ,   514, FieldType.CHAR       );
public static final FixField ExecValuationPoint                   = new FixField("ExecValuationPoint"                ,   515, FieldType.UTC_TIMESTAMP       );
public static final FixField OrderPercent                   = new FixField("OrderPercent"                ,   516, FieldType.DECIMAL       );
public static final FixField OwnershipType                   = new FixField("OwnershipType"                ,   517, FieldType.CHAR       );
public static final FixField NoContAmts                   = new FixField("NoContAmts"                ,   518, FieldType.GROUP_SIZE       );
public static final FixField ContAmtType                   = new FixField("ContAmtType"                ,   519, FieldType.INTEGER       );
public static final FixField ContAmtValue                   = new FixField("ContAmtValue"                ,   520, FieldType.DECIMAL       );
public static final FixField ContAmtCurr                   = new FixField("ContAmtCurr"                ,   521, FieldType.STRING       );
public static final FixField OwnerType                   = new FixField("OwnerType"                ,   522, FieldType.INTEGER       );
public static final FixField PartySubID                   = new FixField("PartySubID"                ,   523, FieldType.STRING       );
public static final FixField NestedPartyID                   = new FixField("NestedPartyID"                ,   524, FieldType.STRING       );
public static final FixField NestedPartyIDSource                   = new FixField("NestedPartyIDSource"                ,   525, FieldType.CHAR       );
public static final FixField SecondaryClOrdID                   = new FixField("SecondaryClOrdID"                ,   526, FieldType.STRING       );
public static final FixField SecondaryExecID                   = new FixField("SecondaryExecID"                ,   527, FieldType.STRING       );
public static final FixField OrderCapacity                   = new FixField("OrderCapacity"                ,   528, FieldType.CHAR       );
public static final FixField OrderRestrictions                   = new FixField("OrderRestrictions"                ,   529, FieldType.STRING       );
public static final FixField MassCancelRequestType                   = new FixField("MassCancelRequestType"                ,   530, FieldType.CHAR       );
public static final FixField MassCancelResponse                   = new FixField("MassCancelResponse"                ,   531, FieldType.CHAR       );
public static final FixField MassCancelRejectReason                   = new FixField("MassCancelRejectReason"                ,   532, FieldType.CHAR       );
public static final FixField TotalAffectedOrders                   = new FixField("TotalAffectedOrders"                ,   533, FieldType.INTEGER       );
public static final FixField NoAffectedOrders                   = new FixField("NoAffectedOrders"                ,   534, FieldType.INTEGER       );
public static final FixField AffectedOrderID                   = new FixField("AffectedOrderID"                ,   535, FieldType.STRING       );
public static final FixField AffectedSecondaryOrderID                   = new FixField("AffectedSecondaryOrderID"                ,   536, FieldType.STRING       );
public static final FixField QuoteType                   = new FixField("QuoteType"                ,   537, FieldType.INTEGER       );
public static final FixField NestedPartyRole                   = new FixField("NestedPartyRole"                ,   538, FieldType.INTEGER       );
public static final FixField NoNestedPartyIDs                   = new FixField("NoNestedPartyIDs"                ,   539, FieldType.GROUP_SIZE       );
public static final FixField TotalAccruedInterestAmt                   = new FixField("TotalAccruedInterestAmt"                ,   540, FieldType.DECIMAL       );
public static final FixField MaturityDate                   = new FixField("MaturityDate"                ,   541, FieldType.UTC_TIMESTAMP       );
public static final FixField UnderlyingMaturityDate                   = new FixField("UnderlyingMaturityDate"                ,   542, FieldType.UTC_TIMESTAMP       );
public static final FixField InstrRegistry                   = new FixField("InstrRegistry"                ,   543, FieldType.STRING       );
public static final FixField CashMargin                   = new FixField("CashMargin"                ,   544, FieldType.CHAR       );
public static final FixField NestedPartySubID                   = new FixField("NestedPartySubID"                ,   545, FieldType.STRING       );
public static final FixField Scope                   = new FixField("Scope"                ,   546, FieldType.STRING       );
public static final FixField MDImplicitDelete                   = new FixField("MDImplicitDelete"                ,   547, FieldType.BOOLEAN       );
public static final FixField CrossID                   = new FixField("CrossID"                ,   548, FieldType.STRING       );
public static final FixField CrossType                   = new FixField("CrossType"                ,   549, FieldType.INTEGER       );
public static final FixField CrossPrioritization                   = new FixField("CrossPrioritization"                ,   550, FieldType.INTEGER       );
public static final FixField OrigCrossID                   = new FixField("OrigCrossID"                ,   551, FieldType.STRING       );
public static final FixField NoSides                   = new FixField("NoSides"                ,   552, FieldType.GROUP_SIZE       );
public static final FixField Username                   = new FixField("Username"                ,   553, FieldType.STRING       );
public static final FixField Password                   = new FixField("Password"                ,   554, FieldType.STRING       );
public static final FixField NoLegs                   = new FixField("NoLegs"                ,   555, FieldType.GROUP_SIZE       );
public static final FixField LegCurrency                   = new FixField("LegCurrency"                ,   556, FieldType.STRING       );
public static final FixField TotNoSecurityTypes                   = new FixField("TotNoSecurityTypes"                ,   557, FieldType.INTEGER       );
public static final FixField NoSecurityTypes                   = new FixField("NoSecurityTypes"                ,   558, FieldType.GROUP_SIZE       );
public static final FixField SecurityListRequestType                   = new FixField("SecurityListRequestType"                ,   559, FieldType.INTEGER       );
public static final FixField SecurityRequestResult                   = new FixField("SecurityRequestResult"                ,   560, FieldType.INTEGER       );
public static final FixField RoundLot                   = new FixField("RoundLot"                ,   561, FieldType.DECIMAL       );
public static final FixField MinTradeVol                   = new FixField("MinTradeVol"                ,   562, FieldType.DECIMAL       );
public static final FixField MultiLegRptTypeReq                   = new FixField("MultiLegRptTypeReq"                ,   563, FieldType.INTEGER       );
public static final FixField LegPositionEffect                   = new FixField("LegPositionEffect"                ,   564, FieldType.CHAR       );
public static final FixField LegCoveredOrUncovered                   = new FixField("LegCoveredOrUncovered"                ,   565, FieldType.INTEGER       );
public static final FixField LegPrice                   = new FixField("LegPrice"                ,   566, FieldType.DECIMAL       );
public static final FixField TradSesStatusRejReason                   = new FixField("TradSesStatusRejReason"                ,   567, FieldType.INTEGER       );
public static final FixField TradeRequestID                   = new FixField("TradeRequestID"                ,   568, FieldType.STRING       );
public static final FixField TradeRequestType                   = new FixField("TradeRequestType"                ,   569, FieldType.INTEGER       );
public static final FixField PreviouslyReported                   = new FixField("PreviouslyReported"                ,   570, FieldType.BOOLEAN       );
public static final FixField TradeReportID                   = new FixField("TradeReportID"                ,   571, FieldType.STRING       );
public static final FixField TradeReportRefID                   = new FixField("TradeReportRefID"                ,   572, FieldType.STRING       );
public static final FixField MatchStatus                   = new FixField("MatchStatus"                ,   573, FieldType.CHAR       );
public static final FixField MatchType                   = new FixField("MatchType"                ,   574, FieldType.STRING       );
public static final FixField OddLot                   = new FixField("OddLot"                ,   575, FieldType.BOOLEAN       );
public static final FixField NoClearingInstructions                   = new FixField("NoClearingInstructions"                ,   576, FieldType.INTEGER       );
public static final FixField ClearingInstruction                   = new FixField("ClearingInstruction"                ,   577, FieldType.INTEGER       );
public static final FixField TradeInputSource                   = new FixField("TradeInputSource"                ,   578, FieldType.STRING       );
public static final FixField TradeInputDevice                   = new FixField("TradeInputDevice"                ,   579, FieldType.STRING       );
public static final FixField NoDates                   = new FixField("NoDates"                ,   580, FieldType.INTEGER       );
public static final FixField AccountType                   = new FixField("AccountType"                ,   581, FieldType.INTEGER       );
public static final FixField CustOrderCapacity                   = new FixField("CustOrderCapacity"                ,   582, FieldType.INTEGER       );
public static final FixField ClOrdLinkID                   = new FixField("ClOrdLinkID"                ,   583, FieldType.STRING       );
public static final FixField MassStatusReqID                   = new FixField("MassStatusReqID"                ,   584, FieldType.STRING       );
public static final FixField MassStatusReqType                   = new FixField("MassStatusReqType"                ,   585, FieldType.INTEGER       );
public static final FixField OrigOrdModTime                   = new FixField("OrigOrdModTime"                ,   586, FieldType.UTC_TIMESTAMP       );
public static final FixField LegSettlType                   = new FixField("LegSettlType"                ,   587, FieldType.CHAR       );
public static final FixField LegSettlDate                   = new FixField("LegSettlDate"                ,   588, FieldType.UTC_TIMESTAMP       );
public static final FixField DayBookingInst                   = new FixField("DayBookingInst"                ,   589, FieldType.CHAR       );
public static final FixField BookingUnit                   = new FixField("BookingUnit"                ,   590, FieldType.CHAR       );
public static final FixField PreallocMethod                   = new FixField("PreallocMethod"                ,   591, FieldType.CHAR       );
public static final FixField UnderlyingCountryOfIssue                   = new FixField("UnderlyingCountryOfIssue"                ,   592, FieldType.STRING       );
public static final FixField UnderlyingStateOrProvinceOfIssue                   = new FixField("UnderlyingStateOrProvinceOfIssue"                ,   593, FieldType.STRING       );
public static final FixField UnderlyingLocaleOfIssue                   = new FixField("UnderlyingLocaleOfIssue"                ,   594, FieldType.STRING       );
public static final FixField UnderlyingInstrRegistry                   = new FixField("UnderlyingInstrRegistry"                ,   595, FieldType.STRING       );
public static final FixField LegCountryOfIssue                   = new FixField("LegCountryOfIssue"                ,   596, FieldType.STRING       );
public static final FixField LegStateOrProvinceOfIssue                   = new FixField("LegStateOrProvinceOfIssue"                ,   597, FieldType.STRING       );
public static final FixField LegLocaleOfIssue                   = new FixField("LegLocaleOfIssue"                ,   598, FieldType.STRING       );
public static final FixField LegInstrRegistry                   = new FixField("LegInstrRegistry"                ,   599, FieldType.STRING       );
public static final FixField LegSymbol                   = new FixField("LegSymbol"                ,   600, FieldType.STRING       );
public static final FixField LegSymbolSfx                   = new FixField("LegSymbolSfx"                ,   601, FieldType.STRING       );
public static final FixField LegSecurityID                   = new FixField("LegSecurityID"                ,   602, FieldType.STRING       );
public static final FixField LegSecurityIDSource                   = new FixField("LegSecurityIDSource"                ,   603, FieldType.STRING       );
public static final FixField NoLegSecurityAltID                   = new FixField("NoLegSecurityAltID"                ,   604, FieldType.GROUP_SIZE       );
public static final FixField LegSecurityAltID                   = new FixField("LegSecurityAltID"                ,   605, FieldType.STRING       );
public static final FixField LegSecurityAltIDSource                   = new FixField("LegSecurityAltIDSource"                ,   606, FieldType.STRING       );
public static final FixField LegProduct                   = new FixField("LegProduct"                ,   607, FieldType.INTEGER       );
public static final FixField LegCFICode                   = new FixField("LegCFICode"                ,   608, FieldType.STRING       );
public static final FixField LegSecurityType                   = new FixField("LegSecurityType"                ,   609, FieldType.STRING       );
public static final FixField LegMaturityMonthYear                   = new FixField("LegMaturityMonthYear"                ,   610, FieldType.STRING       );
public static final FixField LegMaturityDate                   = new FixField("LegMaturityDate"                ,   611, FieldType.UTC_TIMESTAMP       );
public static final FixField LegStrikePrice                   = new FixField("LegStrikePrice"                ,   612, FieldType.DECIMAL       );
public static final FixField LegOptAttribute                   = new FixField("LegOptAttribute"                ,   613, FieldType.CHAR       );
public static final FixField LegContractMultiplier                   = new FixField("LegContractMultiplier"                ,   614, FieldType.DECIMAL       );
public static final FixField LegCouponRate                   = new FixField("LegCouponRate"                ,   615, FieldType.DECIMAL       );
public static final FixField LegSecurityExchange                   = new FixField("LegSecurityExchange"                ,   616, FieldType.STRING       );
public static final FixField LegIssuer                   = new FixField("LegIssuer"                ,   617, FieldType.STRING       );
public static final FixField EncodedLegIssuerLen                   = new FixField("EncodedLegIssuerLen"                ,   618, FieldType.INTEGER       );
public static final FixField EncodedLegIssuer                   = new FixField("EncodedLegIssuer"                ,   619, FieldType.STRING       );
public static final FixField LegSecurityDesc                   = new FixField("LegSecurityDesc"                ,   620, FieldType.STRING       );
public static final FixField EncodedLegSecurityDescLen                   = new FixField("EncodedLegSecurityDescLen"                ,   621, FieldType.INTEGER       );
public static final FixField EncodedLegSecurityDesc                   = new FixField("EncodedLegSecurityDesc"                ,   622, FieldType.STRING       );
public static final FixField LegRatioQty                   = new FixField("LegRatioQty"                ,   623, FieldType.DECIMAL       );
public static final FixField LegSide                   = new FixField("LegSide"                ,   624, FieldType.CHAR       );
public static final FixField TradingSessionSubID                   = new FixField("TradingSessionSubID"                ,   625, FieldType.STRING       );
public static final FixField AllocType                   = new FixField("AllocType"                ,   626, FieldType.INTEGER       );
public static final FixField NoHops                   = new FixField("NoHops"                ,   627, FieldType.GROUP_SIZE       );
public static final FixField HopCompID                   = new FixField("HopCompID"                ,   628, FieldType.STRING       );
public static final FixField HopSendingTime                   = new FixField("HopSendingTime"                ,   629, FieldType.UTC_TIMESTAMP       );
public static final FixField HopRefID                   = new FixField("HopRefID"                ,   630, FieldType.INTEGER       );
public static final FixField MidPx                   = new FixField("MidPx"                ,   631, FieldType.DECIMAL       );
public static final FixField BidYield                   = new FixField("BidYield"                ,   632, FieldType.DECIMAL       );
public static final FixField MidYield                   = new FixField("MidYield"                ,   633, FieldType.DECIMAL       );
public static final FixField OfferYield                   = new FixField("OfferYield"                ,   634, FieldType.DECIMAL       );
public static final FixField ClearingFeeIndicator                   = new FixField("ClearingFeeIndicator"                ,   635, FieldType.STRING       );
public static final FixField WorkingIndicator                   = new FixField("WorkingIndicator"                ,   636, FieldType.BOOLEAN       );
public static final FixField LegLastPx                   = new FixField("LegLastPx"                ,   637, FieldType.DECIMAL       );
public static final FixField PriorityIndicator                   = new FixField("PriorityIndicator"                ,   638, FieldType.INTEGER       );
public static final FixField PriceImprovement                   = new FixField("PriceImprovement"                ,   639, FieldType.DECIMAL       );
public static final FixField Price2                   = new FixField("Price2"                ,   640, FieldType.DECIMAL       );
public static final FixField LastForwardPoints2                   = new FixField("LastForwardPoints2"                ,   641, FieldType.DECIMAL       );
public static final FixField BidForwardPoints2                   = new FixField("BidForwardPoints2"                ,   642, FieldType.DECIMAL       );
public static final FixField OfferForwardPoints2                   = new FixField("OfferForwardPoints2"                ,   643, FieldType.DECIMAL       );
public static final FixField RFQReqID                   = new FixField("RFQReqID"                ,   644, FieldType.STRING       );
public static final FixField MktBidPx                   = new FixField("MktBidPx"                ,   645, FieldType.DECIMAL       );
public static final FixField MktOfferPx                   = new FixField("MktOfferPx"                ,   646, FieldType.DECIMAL       );
public static final FixField MinBidSize                   = new FixField("MinBidSize"                ,   647, FieldType.DECIMAL       );
public static final FixField MinOfferSize                   = new FixField("MinOfferSize"                ,   648, FieldType.DECIMAL       );
public static final FixField QuoteStatusReqID                   = new FixField("QuoteStatusReqID"                ,   649, FieldType.STRING       );
public static final FixField LegalConfirm                   = new FixField("LegalConfirm"                ,   650, FieldType.BOOLEAN       );
public static final FixField UnderlyingLastPx                   = new FixField("UnderlyingLastPx"                ,   651, FieldType.DECIMAL       );
public static final FixField UnderlyingLastQty                   = new FixField("UnderlyingLastQty"                ,   652, FieldType.DECIMAL       );
public static final FixField LegRefID                   = new FixField("LegRefID"                ,   654, FieldType.STRING       );
public static final FixField ContraLegRefID                   = new FixField("ContraLegRefID"                ,   655, FieldType.STRING       );
public static final FixField SettlCurrBidFxRate                   = new FixField("SettlCurrBidFxRate"                ,   656, FieldType.DECIMAL       );
public static final FixField SettlCurrOfferFxRate                   = new FixField("SettlCurrOfferFxRate"                ,   657, FieldType.DECIMAL       );
public static final FixField QuoteRequestRejectReason                   = new FixField("QuoteRequestRejectReason"                ,   658, FieldType.INTEGER       );
public static final FixField SideComplianceID                   = new FixField("SideComplianceID"                ,   659, FieldType.STRING       );
public static final FixField AcctIDSource                   = new FixField("AcctIDSource"                ,   660, FieldType.INTEGER       );
public static final FixField AllocAcctIDSource                   = new FixField("AllocAcctIDSource"                ,   661, FieldType.INTEGER       );
public static final FixField BenchmarkPrice                   = new FixField("BenchmarkPrice"                ,   662, FieldType.DECIMAL       );
public static final FixField BenchmarkPriceType                   = new FixField("BenchmarkPriceType"                ,   663, FieldType.INTEGER       );
public static final FixField ConfirmID                   = new FixField("ConfirmID"                ,   664, FieldType.STRING       );
public static final FixField ConfirmStatus                   = new FixField("ConfirmStatus"                ,   665, FieldType.INTEGER       );
public static final FixField ConfirmTransType                   = new FixField("ConfirmTransType"                ,   666, FieldType.INTEGER       );
public static final FixField ContractSettlMonth                   = new FixField("ContractSettlMonth"                ,   667, FieldType.STRING       );
public static final FixField DeliveryForm                   = new FixField("DeliveryForm"                ,   668, FieldType.INTEGER       );
public static final FixField LastParPx                   = new FixField("LastParPx"                ,   669, FieldType.DECIMAL       );
public static final FixField NoLegAllocs                   = new FixField("NoLegAllocs"                ,   670, FieldType.GROUP_SIZE       );
public static final FixField LegAllocAccount                   = new FixField("LegAllocAccount"                ,   671, FieldType.STRING       );
public static final FixField LegIndividualAllocID                   = new FixField("LegIndividualAllocID"                ,   672, FieldType.STRING       );
public static final FixField LegAllocQty                   = new FixField("LegAllocQty"                ,   673, FieldType.DECIMAL       );
public static final FixField LegAllocAcctIDSource                   = new FixField("LegAllocAcctIDSource"                ,   674, FieldType.STRING       );
public static final FixField LegSettlCurrency                   = new FixField("LegSettlCurrency"                ,   675, FieldType.STRING       );
public static final FixField LegBenchmarkCurveCurrency                   = new FixField("LegBenchmarkCurveCurrency"                ,   676, FieldType.STRING       );
public static final FixField LegBenchmarkCurveName                   = new FixField("LegBenchmarkCurveName"                ,   677, FieldType.STRING       );
public static final FixField LegBenchmarkCurvePoint                   = new FixField("LegBenchmarkCurvePoint"                ,   678, FieldType.STRING       );
public static final FixField LegBenchmarkPrice                   = new FixField("LegBenchmarkPrice"                ,   679, FieldType.DECIMAL       );
public static final FixField LegBenchmarkPriceType                   = new FixField("LegBenchmarkPriceType"                ,   680, FieldType.INTEGER       );
public static final FixField LegBidPx                   = new FixField("LegBidPx"                ,   681, FieldType.DECIMAL       );
public static final FixField LegIOIQty                   = new FixField("LegIOIQty"                ,   682, FieldType.STRING       );
public static final FixField NoLegStipulations                   = new FixField("NoLegStipulations"                ,   683, FieldType.GROUP_SIZE       );
public static final FixField LegOfferPx                   = new FixField("LegOfferPx"                ,   684, FieldType.DECIMAL       );
public static final FixField LegOrderQty                   = new FixField("LegOrderQty"                ,   685, FieldType.DECIMAL       );
public static final FixField LegPriceType                   = new FixField("LegPriceType"                ,   686, FieldType.INTEGER       );
public static final FixField LegQty                   = new FixField("LegQty"                ,   687, FieldType.DECIMAL       );
public static final FixField LegStipulationType                   = new FixField("LegStipulationType"                ,   688, FieldType.STRING       );
public static final FixField LegStipulationValue                   = new FixField("LegStipulationValue"                ,   689, FieldType.STRING       );
public static final FixField LegSwapType                   = new FixField("LegSwapType"                ,   690, FieldType.INTEGER       );
public static final FixField Pool                   = new FixField("Pool"                ,   691, FieldType.STRING       );
public static final FixField QuotePriceType                   = new FixField("QuotePriceType"                ,   692, FieldType.INTEGER       );
public static final FixField QuoteRespID                   = new FixField("QuoteRespID"                ,   693, FieldType.STRING       );
public static final FixField QuoteRespType                   = new FixField("QuoteRespType"                ,   694, FieldType.INTEGER       );
public static final FixField QuoteQualifier                   = new FixField("QuoteQualifier"                ,   695, FieldType.CHAR       );
public static final FixField YieldRedemptionDate                   = new FixField("YieldRedemptionDate"                ,   696, FieldType.UTC_TIMESTAMP       );
public static final FixField YieldRedemptionPrice                   = new FixField("YieldRedemptionPrice"                ,   697, FieldType.DECIMAL       );
public static final FixField YieldRedemptionPriceType                   = new FixField("YieldRedemptionPriceType"                ,   698, FieldType.INTEGER       );
public static final FixField BenchmarkSecurityID                   = new FixField("BenchmarkSecurityID"                ,   699, FieldType.STRING       );
public static final FixField ReversalIndicator                   = new FixField("ReversalIndicator"                ,   700, FieldType.BOOLEAN       );
public static final FixField YieldCalcDate                   = new FixField("YieldCalcDate"                ,   701, FieldType.UTC_TIMESTAMP       );
public static final FixField NoPositions                   = new FixField("NoPositions"                ,   702, FieldType.GROUP_SIZE       );
public static final FixField PosType                   = new FixField("PosType"                ,   703, FieldType.STRING       );
public static final FixField LongQty                   = new FixField("LongQty"                ,   704, FieldType.DECIMAL       );
public static final FixField ShortQty                   = new FixField("ShortQty"                ,   705, FieldType.DECIMAL       );
public static final FixField PosQtyStatus                   = new FixField("PosQtyStatus"                ,   706, FieldType.INTEGER       );
public static final FixField PosAmtType                   = new FixField("PosAmtType"                ,   707, FieldType.STRING       );
public static final FixField PosAmt                   = new FixField("PosAmt"                ,   708, FieldType.DECIMAL       );
public static final FixField PosTransType                   = new FixField("PosTransType"                ,   709, FieldType.INTEGER       );
public static final FixField PosReqID                   = new FixField("PosReqID"                ,   710, FieldType.STRING       );
public static final FixField NoUnderlyings                   = new FixField("NoUnderlyings"                ,   711, FieldType.GROUP_SIZE       );
public static final FixField PosMaintAction                   = new FixField("PosMaintAction"                ,   712, FieldType.INTEGER       );
public static final FixField OrigPosReqRefID                   = new FixField("OrigPosReqRefID"                ,   713, FieldType.STRING       );
public static final FixField PosMaintRptRefID                   = new FixField("PosMaintRptRefID"                ,   714, FieldType.STRING       );
public static final FixField ClearingBusinessDate                   = new FixField("ClearingBusinessDate"                ,   715, FieldType.UTC_TIMESTAMP       );
public static final FixField SettlSessID                   = new FixField("SettlSessID"                ,   716, FieldType.STRING       );
public static final FixField SettlSessSubID                   = new FixField("SettlSessSubID"                ,   717, FieldType.STRING       );
public static final FixField AdjustmentType                   = new FixField("AdjustmentType"                ,   718, FieldType.INTEGER       );
public static final FixField ContraryInstructionIndicator                   = new FixField("ContraryInstructionIndicator"                ,   719, FieldType.BOOLEAN       );
public static final FixField PriorSpreadIndicator                   = new FixField("PriorSpreadIndicator"                ,   720, FieldType.BOOLEAN       );
public static final FixField PosMaintRptID                   = new FixField("PosMaintRptID"                ,   721, FieldType.STRING       );
public static final FixField PosMaintStatus                   = new FixField("PosMaintStatus"                ,   722, FieldType.INTEGER       );
public static final FixField PosMaintResult                   = new FixField("PosMaintResult"                ,   723, FieldType.INTEGER       );
public static final FixField PosReqType                   = new FixField("PosReqType"                ,   724, FieldType.INTEGER       );
public static final FixField ResponseTransportType                   = new FixField("ResponseTransportType"                ,   725, FieldType.INTEGER       );
public static final FixField ResponseDestination                   = new FixField("ResponseDestination"                ,   726, FieldType.STRING       );
public static final FixField TotalNumPosReports                   = new FixField("TotalNumPosReports"                ,   727, FieldType.INTEGER       );
public static final FixField PosReqResult                   = new FixField("PosReqResult"                ,   728, FieldType.INTEGER       );
public static final FixField PosReqStatus                   = new FixField("PosReqStatus"                ,   729, FieldType.INTEGER       );
public static final FixField SettlPrice                   = new FixField("SettlPrice"                ,   730, FieldType.DECIMAL       );
public static final FixField SettlPriceType                   = new FixField("SettlPriceType"                ,   731, FieldType.INTEGER       );
public static final FixField UnderlyingSettlPrice                   = new FixField("UnderlyingSettlPrice"                ,   732, FieldType.DECIMAL       );
public static final FixField UnderlyingSettlPriceType                   = new FixField("UnderlyingSettlPriceType"                ,   733, FieldType.INTEGER       );
public static final FixField PriorSettlPrice                   = new FixField("PriorSettlPrice"                ,   734, FieldType.DECIMAL       );
public static final FixField NoQuoteQualifiers                   = new FixField("NoQuoteQualifiers"                ,   735, FieldType.GROUP_SIZE       );
public static final FixField AllocSettlCurrency                   = new FixField("AllocSettlCurrency"                ,   736, FieldType.STRING       );
public static final FixField AllocSettlCurrAmt                   = new FixField("AllocSettlCurrAmt"                ,   737, FieldType.DECIMAL       );
public static final FixField InterestAtMaturity                   = new FixField("InterestAtMaturity"                ,   738, FieldType.DECIMAL       );
public static final FixField LegDatedDate                   = new FixField("LegDatedDate"                ,   739, FieldType.UTC_TIMESTAMP       );
public static final FixField LegPool                   = new FixField("LegPool"                ,   740, FieldType.STRING       );
public static final FixField AllocInterestAtMaturity                   = new FixField("AllocInterestAtMaturity"                ,   741, FieldType.DECIMAL       );
public static final FixField AllocAccruedInterestAmt                   = new FixField("AllocAccruedInterestAmt"                ,   742, FieldType.DECIMAL       );
public static final FixField DeliveryDate                   = new FixField("DeliveryDate"                ,   743, FieldType.UTC_TIMESTAMP       );
public static final FixField AssignmentMethod                   = new FixField("AssignmentMethod"                ,   744, FieldType.CHAR       );
public static final FixField AssignmentUnit                   = new FixField("AssignmentUnit"                ,   745, FieldType.DECIMAL       );
public static final FixField OpenInterest                   = new FixField("OpenInterest"                ,   746, FieldType.DECIMAL       );
public static final FixField ExerciseMethod                   = new FixField("ExerciseMethod"                ,   747, FieldType.CHAR       );
public static final FixField TotNumTradeReports                   = new FixField("TotNumTradeReports"                ,   748, FieldType.INTEGER       );
public static final FixField TradeRequestResult                   = new FixField("TradeRequestResult"                ,   749, FieldType.INTEGER       );
public static final FixField TradeRequestStatus                   = new FixField("TradeRequestStatus"                ,   750, FieldType.INTEGER       );
public static final FixField TradeReportRejectReason                   = new FixField("TradeReportRejectReason"                ,   751, FieldType.INTEGER       );
public static final FixField SideMultiLegReportingType                   = new FixField("SideMultiLegReportingType"                ,   752, FieldType.INTEGER       );
public static final FixField NoPosAmt                   = new FixField("NoPosAmt"                ,   753, FieldType.GROUP_SIZE       );
public static final FixField AutoAcceptIndicator                   = new FixField("AutoAcceptIndicator"                ,   754, FieldType.BOOLEAN       );
public static final FixField AllocReportID                   = new FixField("AllocReportID"                ,   755, FieldType.STRING       );
public static final FixField NoNested2PartyIDs                   = new FixField("NoNested2PartyIDs"                ,   756, FieldType.GROUP_SIZE       );
public static final FixField Nested2PartyID                   = new FixField("Nested2PartyID"                ,   757, FieldType.STRING       );
public static final FixField Nested2PartyIDSource                   = new FixField("Nested2PartyIDSource"                ,   758, FieldType.CHAR       );
public static final FixField Nested2PartyRole                   = new FixField("Nested2PartyRole"                ,   759, FieldType.INTEGER       );
public static final FixField Nested2PartySubID                   = new FixField("Nested2PartySubID"                ,   760, FieldType.STRING       );
public static final FixField BenchmarkSecurityIDSource                   = new FixField("BenchmarkSecurityIDSource"                ,   761, FieldType.STRING       );
public static final FixField SecuritySubType                   = new FixField("SecuritySubType"                ,   762, FieldType.STRING       );
public static final FixField UnderlyingSecuritySubType                   = new FixField("UnderlyingSecuritySubType"                ,   763, FieldType.STRING       );
public static final FixField LegSecuritySubType                   = new FixField("LegSecuritySubType"                ,   764, FieldType.STRING       );
public static final FixField AllowableOneSidednessPct                   = new FixField("AllowableOneSidednessPct"                ,   765, FieldType.DECIMAL       );
public static final FixField AllowableOneSidednessValue                   = new FixField("AllowableOneSidednessValue"                ,   766, FieldType.DECIMAL       );
public static final FixField AllowableOneSidednessCurr                   = new FixField("AllowableOneSidednessCurr"                ,   767, FieldType.STRING       );
public static final FixField NoTrdRegTimestamps                   = new FixField("NoTrdRegTimestamps"                ,   768, FieldType.GROUP_SIZE       );
public static final FixField TrdRegTimestamp                   = new FixField("TrdRegTimestamp"                ,   769, FieldType.UTC_TIMESTAMP       );
public static final FixField TrdRegTimestampType                   = new FixField("TrdRegTimestampType"                ,   770, FieldType.INTEGER       );
public static final FixField TrdRegTimestampOrigin                   = new FixField("TrdRegTimestampOrigin"                ,   771, FieldType.STRING       );
public static final FixField ConfirmRefID                   = new FixField("ConfirmRefID"                ,   772, FieldType.STRING       );
public static final FixField ConfirmType                   = new FixField("ConfirmType"                ,   773, FieldType.INTEGER       );
public static final FixField ConfirmRejReason                   = new FixField("ConfirmRejReason"                ,   774, FieldType.INTEGER       );
public static final FixField BookingType                   = new FixField("BookingType"                ,   775, FieldType.INTEGER       );
public static final FixField IndividualAllocRejCode                   = new FixField("IndividualAllocRejCode"                ,   776, FieldType.INTEGER       );
public static final FixField SettlInstMsgID                   = new FixField("SettlInstMsgID"                ,   777, FieldType.STRING       );
public static final FixField NoSettlInst                   = new FixField("NoSettlInst"                ,   778, FieldType.GROUP_SIZE       );
public static final FixField LastUpdateTime                   = new FixField("LastUpdateTime"                ,   779, FieldType.UTC_TIMESTAMP       );
public static final FixField AllocSettlInstType                   = new FixField("AllocSettlInstType"                ,   780, FieldType.INTEGER       );
public static final FixField NoSettlPartyIDs                   = new FixField("NoSettlPartyIDs"                ,   781, FieldType.GROUP_SIZE       );
public static final FixField SettlPartyID                   = new FixField("SettlPartyID"                ,   782, FieldType.STRING       );
public static final FixField SettlPartyIDSource                   = new FixField("SettlPartyIDSource"                ,   783, FieldType.CHAR       );
public static final FixField SettlPartyRole                   = new FixField("SettlPartyRole"                ,   784, FieldType.INTEGER       );
public static final FixField SettlPartySubID                   = new FixField("SettlPartySubID"                ,   785, FieldType.STRING       );
public static final FixField SettlPartySubIDType                   = new FixField("SettlPartySubIDType"                ,   786, FieldType.INTEGER       );
public static final FixField DlvyInstType                   = new FixField("DlvyInstType"                ,   787, FieldType.CHAR       );
public static final FixField TerminationType                   = new FixField("TerminationType"                ,   788, FieldType.INTEGER       );
public static final FixField NextExpectedMsgSeqNum                   = new FixField("NextExpectedMsgSeqNum"                ,   789, FieldType.INTEGER       );
public static final FixField OrdStatusReqID                   = new FixField("OrdStatusReqID"                ,   790, FieldType.STRING       );
public static final FixField SettlInstReqID                   = new FixField("SettlInstReqID"                ,   791, FieldType.STRING       );
public static final FixField SettlInstReqRejCode                   = new FixField("SettlInstReqRejCode"                ,   792, FieldType.INTEGER       );
public static final FixField SecondaryAllocID                   = new FixField("SecondaryAllocID"                ,   793, FieldType.STRING       );
public static final FixField AllocReportType                   = new FixField("AllocReportType"                ,   794, FieldType.INTEGER       );
public static final FixField AllocReportRefID                   = new FixField("AllocReportRefID"                ,   795, FieldType.STRING       );
public static final FixField AllocCancReplaceReason                   = new FixField("AllocCancReplaceReason"                ,   796, FieldType.INTEGER       );
public static final FixField CopyMsgIndicator                   = new FixField("CopyMsgIndicator"                ,   797, FieldType.BOOLEAN       );
public static final FixField AllocAccountType                   = new FixField("AllocAccountType"                ,   798, FieldType.INTEGER       );
public static final FixField OrderAvgPx                   = new FixField("OrderAvgPx"                ,   799, FieldType.DECIMAL       );
public static final FixField OrderBookingQty                   = new FixField("OrderBookingQty"                ,   800, FieldType.DECIMAL       );
public static final FixField NoSettlPartySubIDs                   = new FixField("NoSettlPartySubIDs"                ,   801, FieldType.GROUP_SIZE       );
public static final FixField NoPartySubIDs                   = new FixField("NoPartySubIDs"                ,   802, FieldType.GROUP_SIZE       );
public static final FixField PartySubIDType                   = new FixField("PartySubIDType"                ,   803, FieldType.INTEGER       );
public static final FixField NoNestedPartySubIDs                   = new FixField("NoNestedPartySubIDs"                ,   804, FieldType.GROUP_SIZE       );
public static final FixField NestedPartySubIDType                   = new FixField("NestedPartySubIDType"                ,   805, FieldType.INTEGER       );
public static final FixField NoNested2PartySubIDs                   = new FixField("NoNested2PartySubIDs"                ,   806, FieldType.GROUP_SIZE       );
public static final FixField Nested2PartySubIDType                   = new FixField("Nested2PartySubIDType"                ,   807, FieldType.INTEGER       );
public static final FixField AllocIntermedReqType                   = new FixField("AllocIntermedReqType"                ,   808, FieldType.INTEGER       );
public static final FixField UnderlyingPx                   = new FixField("UnderlyingPx"                ,   810, FieldType.DECIMAL       );
public static final FixField PriceDelta                   = new FixField("PriceDelta"                ,   811, FieldType.DECIMAL       );
public static final FixField ApplQueueMax                   = new FixField("ApplQueueMax"                ,   812, FieldType.INTEGER       );
public static final FixField ApplQueueDepth                   = new FixField("ApplQueueDepth"                ,   813, FieldType.INTEGER       );
public static final FixField ApplQueueResolution                   = new FixField("ApplQueueResolution"                ,   814, FieldType.INTEGER       );
public static final FixField ApplQueueAction                   = new FixField("ApplQueueAction"                ,   815, FieldType.INTEGER       );
public static final FixField NoAltMDSource                   = new FixField("NoAltMDSource"                ,   816, FieldType.GROUP_SIZE       );
public static final FixField AltMDSourceID                   = new FixField("AltMDSourceID"                ,   817, FieldType.STRING       );
public static final FixField SecondaryTradeReportID                   = new FixField("SecondaryTradeReportID"                ,   818, FieldType.STRING       );
public static final FixField AvgPxIndicator                   = new FixField("AvgPxIndicator"                ,   819, FieldType.INTEGER       );
public static final FixField TradeLinkID                   = new FixField("TradeLinkID"                ,   820, FieldType.STRING       );
public static final FixField OrderInputDevice                   = new FixField("OrderInputDevice"                ,   821, FieldType.STRING       );
public static final FixField UnderlyingTradingSessionID                   = new FixField("UnderlyingTradingSessionID"                ,   822, FieldType.STRING       );
public static final FixField UnderlyingTradingSessionSubID                   = new FixField("UnderlyingTradingSessionSubID"                ,   823, FieldType.STRING       );
public static final FixField TradeLegRefID                   = new FixField("TradeLegRefID"                ,   824, FieldType.STRING       );
public static final FixField ExchangeRule                   = new FixField("ExchangeRule"                ,   825, FieldType.STRING       );
public static final FixField TradeAllocIndicator                   = new FixField("TradeAllocIndicator"                ,   826, FieldType.INTEGER       );
public static final FixField ExpirationCycle                   = new FixField("ExpirationCycle"                ,   827, FieldType.INTEGER       );
public static final FixField TrdType                   = new FixField("TrdType"                ,   828, FieldType.INTEGER       );
public static final FixField TrdSubType                   = new FixField("TrdSubType"                ,   829, FieldType.INTEGER       );
public static final FixField TransferReason                   = new FixField("TransferReason"                ,   830, FieldType.STRING       );
public static final FixField AsgnReqID                   = new FixField("AsgnReqID"                ,   831, FieldType.STRING       );
public static final FixField TotNumAssignmentReports                   = new FixField("TotNumAssignmentReports"                ,   832, FieldType.INTEGER       );
public static final FixField AsgnRptID                   = new FixField("AsgnRptID"                ,   833, FieldType.STRING       );
public static final FixField ThresholdAmount                   = new FixField("ThresholdAmount"                ,   834, FieldType.DECIMAL       );
public static final FixField PegMoveType                   = new FixField("PegMoveType"                ,   835, FieldType.INTEGER       );
public static final FixField PegOffsetType                   = new FixField("PegOffsetType"                ,   836, FieldType.INTEGER       );
public static final FixField PegLimitType                   = new FixField("PegLimitType"                ,   837, FieldType.INTEGER       );
public static final FixField PegRoundDirection                   = new FixField("PegRoundDirection"                ,   838, FieldType.INTEGER       );
public static final FixField PeggedPrice                   = new FixField("PeggedPrice"                ,   839, FieldType.DECIMAL       );
public static final FixField PegScope                   = new FixField("PegScope"                ,   840, FieldType.INTEGER       );
public static final FixField DiscretionMoveType                   = new FixField("DiscretionMoveType"                ,   841, FieldType.INTEGER       );
public static final FixField DiscretionOffsetType                   = new FixField("DiscretionOffsetType"                ,   842, FieldType.INTEGER       );
public static final FixField DiscretionLimitType                   = new FixField("DiscretionLimitType"                ,   843, FieldType.INTEGER       );
public static final FixField DiscretionRoundDirection                   = new FixField("DiscretionRoundDirection"                ,   844, FieldType.INTEGER       );
public static final FixField DiscretionPrice                   = new FixField("DiscretionPrice"                ,   845, FieldType.DECIMAL       );
public static final FixField DiscretionScope                   = new FixField("DiscretionScope"                ,   846, FieldType.INTEGER       );
public static final FixField TargetStrategy                   = new FixField("TargetStrategy"                ,   847, FieldType.INTEGER       );
public static final FixField TargetStrategyParameters                   = new FixField("TargetStrategyParameters"                ,   848, FieldType.STRING       );
public static final FixField ParticipationRate                   = new FixField("ParticipationRate"                ,   849, FieldType.DECIMAL       );
public static final FixField TargetStrategyPerformance                   = new FixField("TargetStrategyPerformance"                ,   850, FieldType.DECIMAL       );
public static final FixField LastLiquidityInd                   = new FixField("LastLiquidityInd"                ,   851, FieldType.INTEGER       );
public static final FixField PublishTrdIndicator                   = new FixField("PublishTrdIndicator"                ,   852, FieldType.BOOLEAN       );
public static final FixField ShortSaleReason                   = new FixField("ShortSaleReason"                ,   853, FieldType.INTEGER       );
public static final FixField QtyType                   = new FixField("QtyType"                ,   854, FieldType.INTEGER       );
public static final FixField SecondaryTrdType                   = new FixField("SecondaryTrdType"                ,   855, FieldType.INTEGER       );
public static final FixField TradeReportType                   = new FixField("TradeReportType"                ,   856, FieldType.INTEGER       );
public static final FixField AllocNoOrdersType                   = new FixField("AllocNoOrdersType"                ,   857, FieldType.INTEGER       );
public static final FixField SharedCommission                   = new FixField("SharedCommission"                ,   858, FieldType.DECIMAL       );
public static final FixField ConfirmReqID                   = new FixField("ConfirmReqID"                ,   859, FieldType.STRING       );
public static final FixField AvgParPx                   = new FixField("AvgParPx"                ,   860, FieldType.DECIMAL       );
public static final FixField ReportedPx                   = new FixField("ReportedPx"                ,   861, FieldType.DECIMAL       );
public static final FixField NoCapacities                   = new FixField("NoCapacities"                ,   862, FieldType.GROUP_SIZE       );
public static final FixField OrderCapacityQty                   = new FixField("OrderCapacityQty"                ,   863, FieldType.DECIMAL       );
public static final FixField NoEvents                   = new FixField("NoEvents"                ,   864, FieldType.GROUP_SIZE       );
public static final FixField EventType                   = new FixField("EventType"                ,   865, FieldType.INTEGER       );
public static final FixField EventDate                   = new FixField("EventDate"                ,   866, FieldType.UTC_TIMESTAMP       );
public static final FixField EventPx                   = new FixField("EventPx"                ,   867, FieldType.DECIMAL       );
public static final FixField EventText                   = new FixField("EventText"                ,   868, FieldType.STRING       );
public static final FixField PctAtRisk                   = new FixField("PctAtRisk"                ,   869, FieldType.DECIMAL       );
public static final FixField NoInstrAttrib                   = new FixField("NoInstrAttrib"                ,   870, FieldType.GROUP_SIZE       );
public static final FixField InstrAttribType                   = new FixField("InstrAttribType"                ,   871, FieldType.INTEGER       );
public static final FixField InstrAttribValue                   = new FixField("InstrAttribValue"                ,   872, FieldType.STRING       );
public static final FixField DatedDate                   = new FixField("DatedDate"                ,   873, FieldType.UTC_TIMESTAMP       );
public static final FixField InterestAccrualDate                   = new FixField("InterestAccrualDate"                ,   874, FieldType.UTC_TIMESTAMP       );
public static final FixField CPProgram                   = new FixField("CPProgram"                ,   875, FieldType.INTEGER       );
public static final FixField CPRegType                   = new FixField("CPRegType"                ,   876, FieldType.STRING       );
public static final FixField UnderlyingCPProgram                   = new FixField("UnderlyingCPProgram"                ,   877, FieldType.STRING       );
public static final FixField UnderlyingCPRegType                   = new FixField("UnderlyingCPRegType"                ,   878, FieldType.STRING       );
public static final FixField UnderlyingQty                   = new FixField("UnderlyingQty"                ,   879, FieldType.DECIMAL       );
public static final FixField TrdMatchID                   = new FixField("TrdMatchID"                ,   880, FieldType.STRING       );
public static final FixField SecondaryTradeReportRefID                   = new FixField("SecondaryTradeReportRefID"                ,   881, FieldType.STRING       );
public static final FixField UnderlyingDirtyPrice                   = new FixField("UnderlyingDirtyPrice"                ,   882, FieldType.DECIMAL       );
public static final FixField UnderlyingEndPrice                   = new FixField("UnderlyingEndPrice"                ,   883, FieldType.DECIMAL       );
public static final FixField UnderlyingStartValue                   = new FixField("UnderlyingStartValue"                ,   884, FieldType.DECIMAL       );
public static final FixField UnderlyingCurrentValue                   = new FixField("UnderlyingCurrentValue"                ,   885, FieldType.DECIMAL       );
public static final FixField UnderlyingEndValue                   = new FixField("UnderlyingEndValue"                ,   886, FieldType.DECIMAL       );
public static final FixField NoUnderlyingStips                   = new FixField("NoUnderlyingStips"                ,   887, FieldType.GROUP_SIZE       );
public static final FixField UnderlyingStipType                   = new FixField("UnderlyingStipType"                ,   888, FieldType.STRING       );
public static final FixField UnderlyingStipValue                   = new FixField("UnderlyingStipValue"                ,   889, FieldType.STRING       );
public static final FixField MaturityNetMoney                   = new FixField("MaturityNetMoney"                ,   890, FieldType.DECIMAL       );
public static final FixField MiscFeeBasis                   = new FixField("MiscFeeBasis"                ,   891, FieldType.INTEGER       );
public static final FixField TotNoAllocs                   = new FixField("TotNoAllocs"                ,   892, FieldType.INTEGER       );
public static final FixField LastFragment                   = new FixField("LastFragment"                ,   893, FieldType.BOOLEAN       );
public static final FixField CollReqID                   = new FixField("CollReqID"                ,   894, FieldType.STRING       );
public static final FixField CollAsgnReason                   = new FixField("CollAsgnReason"                ,   895, FieldType.INTEGER       );
public static final FixField CollInquiryQualifier                   = new FixField("CollInquiryQualifier"                ,   896, FieldType.INTEGER       );
public static final FixField NoTrades                   = new FixField("NoTrades"                ,   897, FieldType.GROUP_SIZE       );
public static final FixField MarginRatio                   = new FixField("MarginRatio"                ,   898, FieldType.DECIMAL       );
public static final FixField MarginExcess                   = new FixField("MarginExcess"                ,   899, FieldType.DECIMAL       );
public static final FixField TotalNetValue                   = new FixField("TotalNetValue"                ,   900, FieldType.DECIMAL       );
public static final FixField CashOutstanding                   = new FixField("CashOutstanding"                ,   901, FieldType.DECIMAL       );
public static final FixField CollAsgnID                   = new FixField("CollAsgnID"                ,   902, FieldType.STRING       );
public static final FixField CollAsgnTransType                   = new FixField("CollAsgnTransType"                ,   903, FieldType.INTEGER       );
public static final FixField CollRespID                   = new FixField("CollRespID"                ,   904, FieldType.STRING       );
public static final FixField CollAsgnRespType                   = new FixField("CollAsgnRespType"                ,   905, FieldType.INTEGER       );
public static final FixField CollAsgnRejectReason                   = new FixField("CollAsgnRejectReason"                ,   906, FieldType.INTEGER       );
public static final FixField CollAsgnRefID                   = new FixField("CollAsgnRefID"                ,   907, FieldType.STRING       );
public static final FixField CollRptID                   = new FixField("CollRptID"                ,   908, FieldType.STRING       );
public static final FixField CollInquiryID                   = new FixField("CollInquiryID"                ,   909, FieldType.STRING       );
public static final FixField CollStatus                   = new FixField("CollStatus"                ,   910, FieldType.INTEGER       );
public static final FixField TotNumReports                   = new FixField("TotNumReports"                ,   911, FieldType.INTEGER       );
public static final FixField LastRptRequested                   = new FixField("LastRptRequested"                ,   912, FieldType.BOOLEAN       );
public static final FixField AgreementDesc                   = new FixField("AgreementDesc"                ,   913, FieldType.STRING       );
public static final FixField AgreementID                   = new FixField("AgreementID"                ,   914, FieldType.STRING       );
public static final FixField AgreementDate                   = new FixField("AgreementDate"                ,   915, FieldType.UTC_TIMESTAMP       );
public static final FixField StartDate                   = new FixField("StartDate"                ,   916, FieldType.UTC_TIMESTAMP       );
public static final FixField EndDate                   = new FixField("EndDate"                ,   917, FieldType.UTC_TIMESTAMP       );
public static final FixField AgreementCurrency                   = new FixField("AgreementCurrency"                ,   918, FieldType.STRING       );
public static final FixField DeliveryType                   = new FixField("DeliveryType"                ,   919, FieldType.INTEGER       );
public static final FixField EndAccruedInterestAmt                   = new FixField("EndAccruedInterestAmt"                ,   920, FieldType.DECIMAL       );
public static final FixField StartCash                   = new FixField("StartCash"                ,   921, FieldType.DECIMAL       );
public static final FixField EndCash                   = new FixField("EndCash"                ,   922, FieldType.DECIMAL       );
public static final FixField UserRequestID                   = new FixField("UserRequestID"                ,   923, FieldType.STRING       );
public static final FixField UserRequestType                   = new FixField("UserRequestType"                ,   924, FieldType.INTEGER       );
public static final FixField NewPassword                   = new FixField("NewPassword"                ,   925, FieldType.STRING       );
public static final FixField UserStatus                   = new FixField("UserStatus"                ,   926, FieldType.INTEGER       );
public static final FixField UserStatusText                   = new FixField("UserStatusText"                ,   927, FieldType.STRING       );
public static final FixField StatusValue                   = new FixField("StatusValue"                ,   928, FieldType.INTEGER       );
public static final FixField StatusText                   = new FixField("StatusText"                ,   929, FieldType.STRING       );
public static final FixField RefCompID                   = new FixField("RefCompID"                ,   930, FieldType.STRING       );
public static final FixField RefSubID                   = new FixField("RefSubID"                ,   931, FieldType.STRING       );
public static final FixField NetworkResponseID                   = new FixField("NetworkResponseID"                ,   932, FieldType.STRING       );
public static final FixField NetworkRequestID                   = new FixField("NetworkRequestID"                ,   933, FieldType.STRING       );
public static final FixField LastNetworkResponseID                   = new FixField("LastNetworkResponseID"                ,   934, FieldType.STRING       );
public static final FixField NetworkRequestType                   = new FixField("NetworkRequestType"                ,   935, FieldType.INTEGER       );
public static final FixField NoCompIDs                   = new FixField("NoCompIDs"                ,   936, FieldType.GROUP_SIZE       );
public static final FixField NetworkStatusResponseType                   = new FixField("NetworkStatusResponseType"                ,   937, FieldType.INTEGER       );
public static final FixField NoCollInquiryQualifier                   = new FixField("NoCollInquiryQualifier"                ,   938, FieldType.GROUP_SIZE       );
public static final FixField TrdRptStatus                   = new FixField("TrdRptStatus"                ,   939, FieldType.INTEGER       );
public static final FixField AffirmStatus                   = new FixField("AffirmStatus"                ,   940, FieldType.INTEGER       );
public static final FixField UnderlyingStrikeCurrency                   = new FixField("UnderlyingStrikeCurrency"                ,   941, FieldType.STRING       );
public static final FixField LegStrikeCurrency                   = new FixField("LegStrikeCurrency"                ,   942, FieldType.STRING       );
public static final FixField TimeBracket                   = new FixField("TimeBracket"                ,   943, FieldType.STRING       );
public static final FixField CollAction                   = new FixField("CollAction"                ,   944, FieldType.INTEGER       );
public static final FixField CollInquiryStatus                   = new FixField("CollInquiryStatus"                ,   945, FieldType.INTEGER       );
public static final FixField CollInquiryResult                   = new FixField("CollInquiryResult"                ,   946, FieldType.INTEGER       );
public static final FixField StrikeCurrency                   = new FixField("StrikeCurrency"                ,   947, FieldType.STRING       );
public static final FixField NoNested3PartyIDs                   = new FixField("NoNested3PartyIDs"                ,   948, FieldType.GROUP_SIZE       );
public static final FixField Nested3PartyID                   = new FixField("Nested3PartyID"                ,   949, FieldType.STRING       );
public static final FixField Nested3PartyIDSource                   = new FixField("Nested3PartyIDSource"                ,   950, FieldType.CHAR       );
public static final FixField Nested3PartyRole                   = new FixField("Nested3PartyRole"                ,   951, FieldType.INTEGER       );
public static final FixField NoNested3PartySubIDs                   = new FixField("NoNested3PartySubIDs"                ,   952, FieldType.GROUP_SIZE       );
public static final FixField Nested3PartySubID                   = new FixField("Nested3PartySubID"                ,   953, FieldType.STRING       );
public static final FixField Nested3PartySubIDType                   = new FixField("Nested3PartySubIDType"                ,   954, FieldType.INTEGER       );
public static final FixField LegContractSettlMonth                   = new FixField("LegContractSettlMonth"                ,   955, FieldType.STRING       );
public static final FixField LegInterestAccrualDate                   = new FixField("LegInterestAccrualDate"                ,   956, FieldType.UTC_TIMESTAMP       );

private static final FixField[] fieldsByTag = new FixField[]{
Account,
AdvId,
AdvRefID,
AdvSide,
AdvTransType,
AvgPx,
BeginSeqNo,
BeginString,
BodyLength,
CheckSum,
ClOrdID,
Commission,
CommType,
CumQty,
Currency,
EndSeqNo,
ExecID,
ExecInst,
ExecRefID,
null,
HandlInst,
SecurityIDSource,
IOIID,
null,
IOIQltyInd,
IOIRefID,
IOIQty,
IOITransType,
LastCapacity,
LastMkt,
LastPx,
LastQty,
LinesOfText,
MsgSeqNum,
MsgType,
NewSeqNo,
OrderID,
OrderQty,
OrdStatus,
OrdType,
OrigClOrdID,
OrigTime,
PossDupFlag,
Price,
RefSeqNum,
null,
null,
SecurityID,
SenderCompID,
SenderSubID,
null,
SendingTime,
Quantity,
Side,
Symbol,
TargetCompID,
TargetSubID,
Text,
TimeInForce,
TransactTime,
Urgency,
ValidUntilTime,
SettlType,
SettlDate,
SymbolSfx,
ListID,
ListSeqNo,
TotNoOrders,
ListExecInst,
AllocID,
AllocTransType,
RefAllocID,
NoOrders,
AvgPxPrecision,
TradeDate,
null,
PositionEffect,
NoAllocs,
AllocAccount,
AllocQty,
ProcessCode,
NoRpts,
RptSeq,
CxlQty,
NoDlvyInst,
null,
AllocStatus,
AllocRejCode,
Signature,
SecureDataLen,
SecureData,
null,
SignatureLength,
EmailType,
RawDataLength,
RawData,
PossResend,
EncryptMethod,
StopPx,
ExDestination,
null,
CxlRejReason,
OrdRejReason,
IOIQualifier,
WaveNo,
Issuer,
SecurityDesc,
HeartBtInt,
null,
MinQty,
MaxFloor,
TestReqID,
ReportToExch,
LocateReqd,
OnBehalfOfCompID,
OnBehalfOfSubID,
QuoteID,
NetMoney,
SettlCurrAmt,
SettlCurrency,
ForexReq,
OrigSendingTime,
GapFillFlag,
NoExecs,
null,
ExpireTime,
DKReason,
DeliverToCompID,
DeliverToSubID,
IOINaturalFlag,
QuoteReqID,
BidPx,
OfferPx,
BidSize,
OfferSize,
NoMiscFees,
MiscFeeAmt,
MiscFeeCurr,
MiscFeeType,
PrevClosePx,
ResetSeqNumFlag,
SenderLocationID,
TargetLocationID,
OnBehalfOfLocationID,
DeliverToLocationID,
NoRelatedSym,
Subject,
Headline,
URLLink,
ExecType,
LeavesQty,
CashOrderQty,
AllocAvgPx,
AllocNetMoney,
SettlCurrFxRate,
SettlCurrFxRateCalc,
NumDaysInterest,
AccruedInterestRate,
AccruedInterestAmt,
SettlInstMode,
AllocText,
SettlInstID,
SettlInstTransType,
EmailThreadID,
SettlInstSource,
null,
SecurityType,
EffectiveTime,
StandInstDbType,
StandInstDbName,
StandInstDbID,
SettlDeliveryType,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
BidSpotRate,
BidForwardPoints,
OfferSpotRate,
OfferForwardPoints,
OrderQty2,
SettlDate2,
LastSpotRate,
LastForwardPoints,
AllocLinkID,
AllocLinkType,
SecondaryOrderID,
NoIOIQualifiers,
MaturityMonthYear,
PutOrCall,
StrikePrice,
CoveredOrUncovered,
null,
null,
OptAttribute,
SecurityExchange,
NotifyBrokerOfCredit,
AllocHandlInst,
MaxShow,
PegOffsetValue,
XmlDataLen,
XmlData,
SettlInstRefID,
NoRoutingIDs,
RoutingType,
RoutingID,
Spread,
null,
BenchmarkCurveCurrency,
BenchmarkCurveName,
BenchmarkCurvePoint,
CouponRate,
CouponPaymentDate,
IssueDate,
RepurchaseTerm,
RepurchaseRate,
Factor,
TradeOriginationDate,
ExDate,
ContractMultiplier,
NoStipulations,
StipulationType,
StipulationValue,
YieldType,
Yield,
TotalTakedown,
Concession,
RepoCollateralSecurityType,
RedemptionDate,
UnderlyingCouponPaymentDate,
UnderlyingIssueDate,
UnderlyingRepoCollateralSecurityType,
UnderlyingRepurchaseTerm,
UnderlyingRepurchaseRate,
UnderlyingFactor,
UnderlyingRedemptionDate,
LegCouponPaymentDate,
LegIssueDate,
LegRepoCollateralSecurityType,
LegRepurchaseTerm,
LegRepurchaseRate,
LegFactor,
LegRedemptionDate,
CreditRating,
UnderlyingCreditRating,
LegCreditRating,
TradedFlatSwitch,
BasisFeatureDate,
BasisFeaturePrice,
null,
MDReqID,
SubscriptionRequestType,
MarketDepth,
MDUpdateType,
AggregatedBook,
NoMDEntryTypes,
NoMDEntries,
MDEntryType,
MDEntryPx,
MDEntrySize,
MDEntryDate,
MDEntryTime,
TickDirection,
MDMkt,
QuoteCondition,
TradeCondition,
MDEntryID,
MDUpdateAction,
MDEntryRefID,
MDReqRejReason,
MDEntryOriginator,
LocationID,
DeskID,
DeleteReason,
OpenCloseSettlFlag,
SellerDays,
MDEntryBuyer,
MDEntrySeller,
MDEntryPositionNo,
FinancialStatus,
CorporateAction,
DefBidSize,
DefOfferSize,
NoQuoteEntries,
NoQuoteSets,
QuoteStatus,
QuoteCancelType,
QuoteEntryID,
QuoteRejectReason,
QuoteResponseLevel,
QuoteSetID,
QuoteRequestType,
TotNoQuoteEntries,
UnderlyingSecurityIDSource,
UnderlyingIssuer,
UnderlyingSecurityDesc,
UnderlyingSecurityExchange,
UnderlyingSecurityID,
UnderlyingSecurityType,
UnderlyingSymbol,
UnderlyingSymbolSfx,
UnderlyingMaturityMonthYear,
null,
UnderlyingPutOrCall,
UnderlyingStrikePrice,
UnderlyingOptAttribute,
UnderlyingCurrency,
null,
SecurityReqID,
SecurityRequestType,
SecurityResponseID,
SecurityResponseType,
SecurityStatusReqID,
UnsolicitedIndicator,
SecurityTradingStatus,
HaltReason,
InViewOfCommon,
DueToRelated,
BuyVolume,
SellVolume,
HighPx,
LowPx,
Adjustment,
TradSesReqID,
TradingSessionID,
ContraTrader,
TradSesMethod,
TradSesMode,
TradSesStatus,
TradSesStartTime,
TradSesOpenTime,
TradSesPreCloseTime,
TradSesCloseTime,
TradSesEndTime,
NumberOfOrders,
MessageEncoding,
EncodedIssuerLen,
EncodedIssuer,
EncodedSecurityDescLen,
EncodedSecurityDesc,
EncodedListExecInstLen,
EncodedListExecInst,
EncodedTextLen,
EncodedText,
EncodedSubjectLen,
EncodedSubject,
EncodedHeadlineLen,
EncodedHeadline,
EncodedAllocTextLen,
EncodedAllocText,
EncodedUnderlyingIssuerLen,
EncodedUnderlyingIssuer,
EncodedUnderlyingSecurityDescLen,
EncodedUnderlyingSecurityDesc,
AllocPrice,
QuoteSetValidUntilTime,
QuoteEntryRejectReason,
LastMsgSeqNumProcessed,
null,
RefTagID,
RefMsgType,
SessionRejectReason,
BidRequestTransType,
ContraBroker,
ComplianceID,
SolicitedFlag,
ExecRestatementReason,
BusinessRejectRefID,
BusinessRejectReason,
GrossTradeAmt,
NoContraBrokers,
MaxMessageSize,
NoMsgTypes,
MsgDirection,
NoTradingSessions,
TotalVolumeTraded,
DiscretionInst,
DiscretionOffsetValue,
BidID,
ClientBidID,
ListName,
TotNoRelatedSym,
BidType,
NumTickets,
SideValue1,
SideValue2,
NoBidDescriptors,
BidDescriptorType,
BidDescriptor,
SideValueInd,
LiquidityPctLow,
LiquidityPctHigh,
LiquidityValue,
EFPTrackingError,
FairValue,
OutsideIndexPct,
ValueOfFutures,
LiquidityIndType,
WtAverageLiquidity,
ExchangeForPhysical,
OutMainCntryUIndex,
CrossPercent,
ProgRptReqs,
ProgPeriodInterval,
IncTaxInd,
NumBidders,
BidTradeType,
BasisPxType,
NoBidComponents,
Country,
TotNoStrikes,
PriceType,
DayOrderQty,
DayCumQty,
DayAvgPx,
GTBookingInst,
NoStrikes,
ListStatusType,
NetGrossInd,
ListOrderStatus,
ExpireDate,
ListExecInstType,
CxlRejResponseTo,
UnderlyingCouponRate,
UnderlyingContractMultiplier,
ContraTradeQty,
ContraTradeTime,
null,
null,
LiquidityNumSecurities,
MultiLegReportingType,
StrikeTime,
ListStatusText,
EncodedListStatusTextLen,
EncodedListStatusText,
PartyIDSource,
PartyID,
null,
null,
NetChgPrevDay,
PartyRole,
NoPartyIDs,
NoSecurityAltID,
SecurityAltID,
SecurityAltIDSource,
NoUnderlyingSecurityAltID,
UnderlyingSecurityAltID,
UnderlyingSecurityAltIDSource,
Product,
CFICode,
UnderlyingProduct,
UnderlyingCFICode,
TestMessageIndicator,
QuantityType,
BookingRefID,
IndividualAllocID,
RoundingDirection,
RoundingModulus,
CountryOfIssue,
StateOrProvinceOfIssue,
LocaleOfIssue,
NoRegistDtls,
MailingDtls,
InvestorCountryOfResidence,
PaymentRef,
DistribPaymentMethod,
CashDistribCurr,
CommCurrency,
CancellationRights,
MoneyLaunderingStatus,
MailingInst,
TransBkdTime,
ExecPriceType,
ExecPriceAdjustment,
DateOfBirth,
TradeReportTransType,
CardHolderName,
CardNumber,
CardExpDate,
CardIssNum,
PaymentMethod,
RegistAcctType,
Designation,
TaxAdvantageType,
RegistRejReasonText,
FundRenewWaiv,
CashDistribAgentName,
CashDistribAgentCode,
CashDistribAgentAcctNumber,
CashDistribPayRef,
CashDistribAgentAcctName,
CardStartDate,
PaymentDate,
PaymentRemitterID,
RegistStatus,
RegistRejReasonCode,
RegistRefID,
RegistDtls,
NoDistribInsts,
RegistEmail,
DistribPercentage,
RegistID,
RegistTransType,
ExecValuationPoint,
OrderPercent,
OwnershipType,
NoContAmts,
ContAmtType,
ContAmtValue,
ContAmtCurr,
OwnerType,
PartySubID,
NestedPartyID,
NestedPartyIDSource,
SecondaryClOrdID,
SecondaryExecID,
OrderCapacity,
OrderRestrictions,
MassCancelRequestType,
MassCancelResponse,
MassCancelRejectReason,
TotalAffectedOrders,
NoAffectedOrders,
AffectedOrderID,
AffectedSecondaryOrderID,
QuoteType,
NestedPartyRole,
NoNestedPartyIDs,
TotalAccruedInterestAmt,
MaturityDate,
UnderlyingMaturityDate,
InstrRegistry,
CashMargin,
NestedPartySubID,
Scope,
MDImplicitDelete,
CrossID,
CrossType,
CrossPrioritization,
OrigCrossID,
NoSides,
Username,
Password,
NoLegs,
LegCurrency,
TotNoSecurityTypes,
NoSecurityTypes,
SecurityListRequestType,
SecurityRequestResult,
RoundLot,
MinTradeVol,
MultiLegRptTypeReq,
LegPositionEffect,
LegCoveredOrUncovered,
LegPrice,
TradSesStatusRejReason,
TradeRequestID,
TradeRequestType,
PreviouslyReported,
TradeReportID,
TradeReportRefID,
MatchStatus,
MatchType,
OddLot,
NoClearingInstructions,
ClearingInstruction,
TradeInputSource,
TradeInputDevice,
NoDates,
AccountType,
CustOrderCapacity,
ClOrdLinkID,
MassStatusReqID,
MassStatusReqType,
OrigOrdModTime,
LegSettlType,
LegSettlDate,
DayBookingInst,
BookingUnit,
PreallocMethod,
UnderlyingCountryOfIssue,
UnderlyingStateOrProvinceOfIssue,
UnderlyingLocaleOfIssue,
UnderlyingInstrRegistry,
LegCountryOfIssue,
LegStateOrProvinceOfIssue,
LegLocaleOfIssue,
LegInstrRegistry,
LegSymbol,
LegSymbolSfx,
LegSecurityID,
LegSecurityIDSource,
NoLegSecurityAltID,
LegSecurityAltID,
LegSecurityAltIDSource,
LegProduct,
LegCFICode,
LegSecurityType,
LegMaturityMonthYear,
LegMaturityDate,
LegStrikePrice,
LegOptAttribute,
LegContractMultiplier,
LegCouponRate,
LegSecurityExchange,
LegIssuer,
EncodedLegIssuerLen,
EncodedLegIssuer,
LegSecurityDesc,
EncodedLegSecurityDescLen,
EncodedLegSecurityDesc,
LegRatioQty,
LegSide,
TradingSessionSubID,
AllocType,
NoHops,
HopCompID,
HopSendingTime,
HopRefID,
MidPx,
BidYield,
MidYield,
OfferYield,
ClearingFeeIndicator,
WorkingIndicator,
LegLastPx,
PriorityIndicator,
PriceImprovement,
Price2,
LastForwardPoints2,
BidForwardPoints2,
OfferForwardPoints2,
RFQReqID,
MktBidPx,
MktOfferPx,
MinBidSize,
MinOfferSize,
QuoteStatusReqID,
LegalConfirm,
UnderlyingLastPx,
UnderlyingLastQty,
null,
LegRefID,
ContraLegRefID,
SettlCurrBidFxRate,
SettlCurrOfferFxRate,
QuoteRequestRejectReason,
SideComplianceID,
AcctIDSource,
AllocAcctIDSource,
BenchmarkPrice,
BenchmarkPriceType,
ConfirmID,
ConfirmStatus,
ConfirmTransType,
ContractSettlMonth,
DeliveryForm,
LastParPx,
NoLegAllocs,
LegAllocAccount,
LegIndividualAllocID,
LegAllocQty,
LegAllocAcctIDSource,
LegSettlCurrency,
LegBenchmarkCurveCurrency,
LegBenchmarkCurveName,
LegBenchmarkCurvePoint,
LegBenchmarkPrice,
LegBenchmarkPriceType,
LegBidPx,
LegIOIQty,
NoLegStipulations,
LegOfferPx,
LegOrderQty,
LegPriceType,
LegQty,
LegStipulationType,
LegStipulationValue,
LegSwapType,
Pool,
QuotePriceType,
QuoteRespID,
QuoteRespType,
QuoteQualifier,
YieldRedemptionDate,
YieldRedemptionPrice,
YieldRedemptionPriceType,
BenchmarkSecurityID,
ReversalIndicator,
YieldCalcDate,
NoPositions,
PosType,
LongQty,
ShortQty,
PosQtyStatus,
PosAmtType,
PosAmt,
PosTransType,
PosReqID,
NoUnderlyings,
PosMaintAction,
OrigPosReqRefID,
PosMaintRptRefID,
ClearingBusinessDate,
SettlSessID,
SettlSessSubID,
AdjustmentType,
ContraryInstructionIndicator,
PriorSpreadIndicator,
PosMaintRptID,
PosMaintStatus,
PosMaintResult,
PosReqType,
ResponseTransportType,
ResponseDestination,
TotalNumPosReports,
PosReqResult,
PosReqStatus,
SettlPrice,
SettlPriceType,
UnderlyingSettlPrice,
UnderlyingSettlPriceType,
PriorSettlPrice,
NoQuoteQualifiers,
AllocSettlCurrency,
AllocSettlCurrAmt,
InterestAtMaturity,
LegDatedDate,
LegPool,
AllocInterestAtMaturity,
AllocAccruedInterestAmt,
DeliveryDate,
AssignmentMethod,
AssignmentUnit,
OpenInterest,
ExerciseMethod,
TotNumTradeReports,
TradeRequestResult,
TradeRequestStatus,
TradeReportRejectReason,
SideMultiLegReportingType,
NoPosAmt,
AutoAcceptIndicator,
AllocReportID,
NoNested2PartyIDs,
Nested2PartyID,
Nested2PartyIDSource,
Nested2PartyRole,
Nested2PartySubID,
BenchmarkSecurityIDSource,
SecuritySubType,
UnderlyingSecuritySubType,
LegSecuritySubType,
AllowableOneSidednessPct,
AllowableOneSidednessValue,
AllowableOneSidednessCurr,
NoTrdRegTimestamps,
TrdRegTimestamp,
TrdRegTimestampType,
TrdRegTimestampOrigin,
ConfirmRefID,
ConfirmType,
ConfirmRejReason,
BookingType,
IndividualAllocRejCode,
SettlInstMsgID,
NoSettlInst,
LastUpdateTime,
AllocSettlInstType,
NoSettlPartyIDs,
SettlPartyID,
SettlPartyIDSource,
SettlPartyRole,
SettlPartySubID,
SettlPartySubIDType,
DlvyInstType,
TerminationType,
NextExpectedMsgSeqNum,
OrdStatusReqID,
SettlInstReqID,
SettlInstReqRejCode,
SecondaryAllocID,
AllocReportType,
AllocReportRefID,
AllocCancReplaceReason,
CopyMsgIndicator,
AllocAccountType,
OrderAvgPx,
OrderBookingQty,
NoSettlPartySubIDs,
NoPartySubIDs,
PartySubIDType,
NoNestedPartySubIDs,
NestedPartySubIDType,
NoNested2PartySubIDs,
Nested2PartySubIDType,
AllocIntermedReqType,
null,
UnderlyingPx,
PriceDelta,
ApplQueueMax,
ApplQueueDepth,
ApplQueueResolution,
ApplQueueAction,
NoAltMDSource,
AltMDSourceID,
SecondaryTradeReportID,
AvgPxIndicator,
TradeLinkID,
OrderInputDevice,
UnderlyingTradingSessionID,
UnderlyingTradingSessionSubID,
TradeLegRefID,
ExchangeRule,
TradeAllocIndicator,
ExpirationCycle,
TrdType,
TrdSubType,
TransferReason,
AsgnReqID,
TotNumAssignmentReports,
AsgnRptID,
ThresholdAmount,
PegMoveType,
PegOffsetType,
PegLimitType,
PegRoundDirection,
PeggedPrice,
PegScope,
DiscretionMoveType,
DiscretionOffsetType,
DiscretionLimitType,
DiscretionRoundDirection,
DiscretionPrice,
DiscretionScope,
TargetStrategy,
TargetStrategyParameters,
ParticipationRate,
TargetStrategyPerformance,
LastLiquidityInd,
PublishTrdIndicator,
ShortSaleReason,
QtyType,
SecondaryTrdType,
TradeReportType,
AllocNoOrdersType,
SharedCommission,
ConfirmReqID,
AvgParPx,
ReportedPx,
NoCapacities,
OrderCapacityQty,
NoEvents,
EventType,
EventDate,
EventPx,
EventText,
PctAtRisk,
NoInstrAttrib,
InstrAttribType,
InstrAttribValue,
DatedDate,
InterestAccrualDate,
CPProgram,
CPRegType,
UnderlyingCPProgram,
UnderlyingCPRegType,
UnderlyingQty,
TrdMatchID,
SecondaryTradeReportRefID,
UnderlyingDirtyPrice,
UnderlyingEndPrice,
UnderlyingStartValue,
UnderlyingCurrentValue,
UnderlyingEndValue,
NoUnderlyingStips,
UnderlyingStipType,
UnderlyingStipValue,
MaturityNetMoney,
MiscFeeBasis,
TotNoAllocs,
LastFragment,
CollReqID,
CollAsgnReason,
CollInquiryQualifier,
NoTrades,
MarginRatio,
MarginExcess,
TotalNetValue,
CashOutstanding,
CollAsgnID,
CollAsgnTransType,
CollRespID,
CollAsgnRespType,
CollAsgnRejectReason,
CollAsgnRefID,
CollRptID,
CollInquiryID,
CollStatus,
TotNumReports,
LastRptRequested,
AgreementDesc,
AgreementID,
AgreementDate
};

private static final Map<String,FixField> fieldsByName = new HashMap<>();
static {
fieldsByName.put("Account",Account);
fieldsByName.put("AdvId",AdvId);
fieldsByName.put("AdvRefID",AdvRefID);
fieldsByName.put("AdvSide",AdvSide);
fieldsByName.put("AdvTransType",AdvTransType);
fieldsByName.put("AvgPx",AvgPx);
fieldsByName.put("BeginSeqNo",BeginSeqNo);
fieldsByName.put("BeginString",BeginString);
fieldsByName.put("BodyLength",BodyLength);
fieldsByName.put("CheckSum",CheckSum);
fieldsByName.put("ClOrdID",ClOrdID);
fieldsByName.put("Commission",Commission);
fieldsByName.put("CommType",CommType);
fieldsByName.put("CumQty",CumQty);
fieldsByName.put("Currency",Currency);
fieldsByName.put("EndSeqNo",EndSeqNo);
fieldsByName.put("ExecID",ExecID);
fieldsByName.put("ExecInst",ExecInst);
fieldsByName.put("ExecRefID",ExecRefID);
fieldsByName.put("null",null);
fieldsByName.put("HandlInst",HandlInst);
fieldsByName.put("SecurityIDSource",SecurityIDSource);
fieldsByName.put("IOIID",IOIID);
fieldsByName.put("null",null);
fieldsByName.put("IOIQltyInd",IOIQltyInd);
fieldsByName.put("IOIRefID",IOIRefID);
fieldsByName.put("IOIQty",IOIQty);
fieldsByName.put("IOITransType",IOITransType);
fieldsByName.put("LastCapacity",LastCapacity);
fieldsByName.put("LastMkt",LastMkt);
fieldsByName.put("LastPx",LastPx);
fieldsByName.put("LastQty",LastQty);
fieldsByName.put("LinesOfText",LinesOfText);
fieldsByName.put("MsgSeqNum",MsgSeqNum);
fieldsByName.put("MsgType",MsgType);
fieldsByName.put("NewSeqNo",NewSeqNo);
fieldsByName.put("OrderID",OrderID);
fieldsByName.put("OrderQty",OrderQty);
fieldsByName.put("OrdStatus",OrdStatus);
fieldsByName.put("OrdType",OrdType);
fieldsByName.put("OrigClOrdID",OrigClOrdID);
fieldsByName.put("OrigTime",OrigTime);
fieldsByName.put("PossDupFlag",PossDupFlag);
fieldsByName.put("Price",Price);
fieldsByName.put("RefSeqNum",RefSeqNum);
fieldsByName.put("null",null);
fieldsByName.put("null",null);
fieldsByName.put("SecurityID",SecurityID);
fieldsByName.put("SenderCompID",SenderCompID);
fieldsByName.put("SenderSubID",SenderSubID);
fieldsByName.put("null",null);
fieldsByName.put("SendingTime",SendingTime);
fieldsByName.put("Quantity",Quantity);
fieldsByName.put("Side",Side);
fieldsByName.put("Symbol",Symbol);
fieldsByName.put("TargetCompID",TargetCompID);
fieldsByName.put("TargetSubID",TargetSubID);
fieldsByName.put("Text",Text);
fieldsByName.put("TimeInForce",TimeInForce);
fieldsByName.put("TransactTime",TransactTime);
fieldsByName.put("Urgency",Urgency);
fieldsByName.put("ValidUntilTime",ValidUntilTime);
fieldsByName.put("SettlType",SettlType);
fieldsByName.put("SettlDate",SettlDate);
fieldsByName.put("SymbolSfx",SymbolSfx);
fieldsByName.put("ListID",ListID);
fieldsByName.put("ListSeqNo",ListSeqNo);
fieldsByName.put("TotNoOrders",TotNoOrders);
fieldsByName.put("ListExecInst",ListExecInst);
fieldsByName.put("AllocID",AllocID);
fieldsByName.put("AllocTransType",AllocTransType);
fieldsByName.put("RefAllocID",RefAllocID);
fieldsByName.put("NoOrders",NoOrders);
fieldsByName.put("AvgPxPrecision",AvgPxPrecision);
fieldsByName.put("TradeDate",TradeDate);
fieldsByName.put("null",null);
fieldsByName.put("PositionEffect",PositionEffect);
fieldsByName.put("NoAllocs",NoAllocs);
fieldsByName.put("AllocAccount",AllocAccount);
fieldsByName.put("AllocQty",AllocQty);
fieldsByName.put("ProcessCode",ProcessCode);
fieldsByName.put("NoRpts",NoRpts);
fieldsByName.put("RptSeq",RptSeq);
fieldsByName.put("CxlQty",CxlQty);
fieldsByName.put("NoDlvyInst",NoDlvyInst);
fieldsByName.put("null",null);
fieldsByName.put("AllocStatus",AllocStatus);
fieldsByName.put("AllocRejCode",AllocRejCode);
fieldsByName.put("Signature",Signature);
fieldsByName.put("SecureDataLen",SecureDataLen);
fieldsByName.put("SecureData",SecureData);
fieldsByName.put("null",null);
fieldsByName.put("SignatureLength",SignatureLength);
fieldsByName.put("EmailType",EmailType);
fieldsByName.put("RawDataLength",RawDataLength);
fieldsByName.put("RawData",RawData);
fieldsByName.put("PossResend",PossResend);
fieldsByName.put("EncryptMethod",EncryptMethod);
fieldsByName.put("StopPx",StopPx);
fieldsByName.put("ExDestination",ExDestination);
fieldsByName.put("null",null);
fieldsByName.put("CxlRejReason",CxlRejReason);
fieldsByName.put("OrdRejReason",OrdRejReason);
fieldsByName.put("IOIQualifier",IOIQualifier);
fieldsByName.put("WaveNo",WaveNo);
fieldsByName.put("Issuer",Issuer);
fieldsByName.put("SecurityDesc",SecurityDesc);
fieldsByName.put("HeartBtInt",HeartBtInt);
fieldsByName.put("null",null);
fieldsByName.put("MinQty",MinQty);
fieldsByName.put("MaxFloor",MaxFloor);
fieldsByName.put("TestReqID",TestReqID);
fieldsByName.put("ReportToExch",ReportToExch);
fieldsByName.put("LocateReqd",LocateReqd);
fieldsByName.put("OnBehalfOfCompID",OnBehalfOfCompID);
fieldsByName.put("OnBehalfOfSubID",OnBehalfOfSubID);
fieldsByName.put("QuoteID",QuoteID);
fieldsByName.put("NetMoney",NetMoney);
fieldsByName.put("SettlCurrAmt",SettlCurrAmt);
fieldsByName.put("SettlCurrency",SettlCurrency);
fieldsByName.put("ForexReq",ForexReq);
fieldsByName.put("OrigSendingTime",OrigSendingTime);
fieldsByName.put("GapFillFlag",GapFillFlag);
fieldsByName.put("NoExecs",NoExecs);
fieldsByName.put("null",null);
fieldsByName.put("ExpireTime",ExpireTime);
fieldsByName.put("DKReason",DKReason);
fieldsByName.put("DeliverToCompID",DeliverToCompID);
fieldsByName.put("DeliverToSubID",DeliverToSubID);
fieldsByName.put("IOINaturalFlag",IOINaturalFlag);
fieldsByName.put("QuoteReqID",QuoteReqID);
fieldsByName.put("BidPx",BidPx);
fieldsByName.put("OfferPx",OfferPx);
fieldsByName.put("BidSize",BidSize);
fieldsByName.put("OfferSize",OfferSize);
fieldsByName.put("NoMiscFees",NoMiscFees);
fieldsByName.put("MiscFeeAmt",MiscFeeAmt);
fieldsByName.put("MiscFeeCurr",MiscFeeCurr);
fieldsByName.put("MiscFeeType",MiscFeeType);
fieldsByName.put("PrevClosePx",PrevClosePx);
fieldsByName.put("ResetSeqNumFlag",ResetSeqNumFlag);
fieldsByName.put("SenderLocationID",SenderLocationID);
fieldsByName.put("TargetLocationID",TargetLocationID);
fieldsByName.put("OnBehalfOfLocationID",OnBehalfOfLocationID);
fieldsByName.put("DeliverToLocationID",DeliverToLocationID);
fieldsByName.put("NoRelatedSym",NoRelatedSym);
fieldsByName.put("Subject",Subject);
fieldsByName.put("Headline",Headline);
fieldsByName.put("URLLink",URLLink);
fieldsByName.put("ExecType",ExecType);
fieldsByName.put("LeavesQty",LeavesQty);
fieldsByName.put("CashOrderQty",CashOrderQty);
fieldsByName.put("AllocAvgPx",AllocAvgPx);
fieldsByName.put("AllocNetMoney",AllocNetMoney);
fieldsByName.put("SettlCurrFxRate",SettlCurrFxRate);
fieldsByName.put("SettlCurrFxRateCalc",SettlCurrFxRateCalc);
fieldsByName.put("NumDaysInterest",NumDaysInterest);
fieldsByName.put("AccruedInterestRate",AccruedInterestRate);
fieldsByName.put("AccruedInterestAmt",AccruedInterestAmt);
fieldsByName.put("SettlInstMode",SettlInstMode);
fieldsByName.put("AllocText",AllocText);
fieldsByName.put("SettlInstID",SettlInstID);
fieldsByName.put("SettlInstTransType",SettlInstTransType);
fieldsByName.put("EmailThreadID",EmailThreadID);
fieldsByName.put("SettlInstSource",SettlInstSource);
fieldsByName.put("null",null);
fieldsByName.put("SecurityType",SecurityType);
fieldsByName.put("EffectiveTime",EffectiveTime);
fieldsByName.put("StandInstDbType",StandInstDbType);
fieldsByName.put("StandInstDbName",StandInstDbName);
fieldsByName.put("StandInstDbID",StandInstDbID);
fieldsByName.put("SettlDeliveryType",SettlDeliveryType);
fieldsByName.put("null",null);
fieldsByName.put("null",null);
fieldsByName.put("null",null);
fieldsByName.put("null",null);
fieldsByName.put("null",null);
fieldsByName.put("null",null);
fieldsByName.put("null",null);
fieldsByName.put("null",null);
fieldsByName.put("null",null);
fieldsByName.put("null",null);
fieldsByName.put("null",null);
fieldsByName.put("null",null);
fieldsByName.put("null",null);
fieldsByName.put("null",null);
fieldsByName.put("null",null);
fieldsByName.put("BidSpotRate",BidSpotRate);
fieldsByName.put("BidForwardPoints",BidForwardPoints);
fieldsByName.put("OfferSpotRate",OfferSpotRate);
fieldsByName.put("OfferForwardPoints",OfferForwardPoints);
fieldsByName.put("OrderQty2",OrderQty2);
fieldsByName.put("SettlDate2",SettlDate2);
fieldsByName.put("LastSpotRate",LastSpotRate);
fieldsByName.put("LastForwardPoints",LastForwardPoints);
fieldsByName.put("AllocLinkID",AllocLinkID);
fieldsByName.put("AllocLinkType",AllocLinkType);
fieldsByName.put("SecondaryOrderID",SecondaryOrderID);
fieldsByName.put("NoIOIQualifiers",NoIOIQualifiers);
fieldsByName.put("MaturityMonthYear",MaturityMonthYear);
fieldsByName.put("PutOrCall",PutOrCall);
fieldsByName.put("StrikePrice",StrikePrice);
fieldsByName.put("CoveredOrUncovered",CoveredOrUncovered);
fieldsByName.put("null",null);
fieldsByName.put("null",null);
fieldsByName.put("OptAttribute",OptAttribute);
fieldsByName.put("SecurityExchange",SecurityExchange);
fieldsByName.put("NotifyBrokerOfCredit",NotifyBrokerOfCredit);
fieldsByName.put("AllocHandlInst",AllocHandlInst);
fieldsByName.put("MaxShow",MaxShow);
fieldsByName.put("PegOffsetValue",PegOffsetValue);
fieldsByName.put("XmlDataLen",XmlDataLen);
fieldsByName.put("XmlData",XmlData);
fieldsByName.put("SettlInstRefID",SettlInstRefID);
fieldsByName.put("NoRoutingIDs",NoRoutingIDs);
fieldsByName.put("RoutingType",RoutingType);
fieldsByName.put("RoutingID",RoutingID);
fieldsByName.put("Spread",Spread);
fieldsByName.put("null",null);
fieldsByName.put("BenchmarkCurveCurrency",BenchmarkCurveCurrency);
fieldsByName.put("BenchmarkCurveName",BenchmarkCurveName);
fieldsByName.put("BenchmarkCurvePoint",BenchmarkCurvePoint);
fieldsByName.put("CouponRate",CouponRate);
fieldsByName.put("CouponPaymentDate",CouponPaymentDate);
fieldsByName.put("IssueDate",IssueDate);
fieldsByName.put("RepurchaseTerm",RepurchaseTerm);
fieldsByName.put("RepurchaseRate",RepurchaseRate);
fieldsByName.put("Factor",Factor);
fieldsByName.put("TradeOriginationDate",TradeOriginationDate);
fieldsByName.put("ExDate",ExDate);
fieldsByName.put("ContractMultiplier",ContractMultiplier);
fieldsByName.put("NoStipulations",NoStipulations);
fieldsByName.put("StipulationType",StipulationType);
fieldsByName.put("StipulationValue",StipulationValue);
fieldsByName.put("YieldType",YieldType);
fieldsByName.put("Yield",Yield);
fieldsByName.put("TotalTakedown",TotalTakedown);
fieldsByName.put("Concession",Concession);
fieldsByName.put("RepoCollateralSecurityType",RepoCollateralSecurityType);
fieldsByName.put("RedemptionDate",RedemptionDate);
fieldsByName.put("UnderlyingCouponPaymentDate",UnderlyingCouponPaymentDate);
fieldsByName.put("UnderlyingIssueDate",UnderlyingIssueDate);
fieldsByName.put("UnderlyingRepoCollateralSecurityType",UnderlyingRepoCollateralSecurityType);
fieldsByName.put("UnderlyingRepurchaseTerm",UnderlyingRepurchaseTerm);
fieldsByName.put("UnderlyingRepurchaseRate",UnderlyingRepurchaseRate);
fieldsByName.put("UnderlyingFactor",UnderlyingFactor);
fieldsByName.put("UnderlyingRedemptionDate",UnderlyingRedemptionDate);
fieldsByName.put("LegCouponPaymentDate",LegCouponPaymentDate);
fieldsByName.put("LegIssueDate",LegIssueDate);
fieldsByName.put("LegRepoCollateralSecurityType",LegRepoCollateralSecurityType);
fieldsByName.put("LegRepurchaseTerm",LegRepurchaseTerm);
fieldsByName.put("LegRepurchaseRate",LegRepurchaseRate);
fieldsByName.put("LegFactor",LegFactor);
fieldsByName.put("LegRedemptionDate",LegRedemptionDate);
fieldsByName.put("CreditRating",CreditRating);
fieldsByName.put("UnderlyingCreditRating",UnderlyingCreditRating);
fieldsByName.put("LegCreditRating",LegCreditRating);
fieldsByName.put("TradedFlatSwitch",TradedFlatSwitch);
fieldsByName.put("BasisFeatureDate",BasisFeatureDate);
fieldsByName.put("BasisFeaturePrice",BasisFeaturePrice);
fieldsByName.put("null",null);
fieldsByName.put("MDReqID",MDReqID);
fieldsByName.put("SubscriptionRequestType",SubscriptionRequestType);
fieldsByName.put("MarketDepth",MarketDepth);
fieldsByName.put("MDUpdateType",MDUpdateType);
fieldsByName.put("AggregatedBook",AggregatedBook);
fieldsByName.put("NoMDEntryTypes",NoMDEntryTypes);
fieldsByName.put("NoMDEntries",NoMDEntries);
fieldsByName.put("MDEntryType",MDEntryType);
fieldsByName.put("MDEntryPx",MDEntryPx);
fieldsByName.put("MDEntrySize",MDEntrySize);
fieldsByName.put("MDEntryDate",MDEntryDate);
fieldsByName.put("MDEntryTime",MDEntryTime);
fieldsByName.put("TickDirection",TickDirection);
fieldsByName.put("MDMkt",MDMkt);
fieldsByName.put("QuoteCondition",QuoteCondition);
fieldsByName.put("TradeCondition",TradeCondition);
fieldsByName.put("MDEntryID",MDEntryID);
fieldsByName.put("MDUpdateAction",MDUpdateAction);
fieldsByName.put("MDEntryRefID",MDEntryRefID);
fieldsByName.put("MDReqRejReason",MDReqRejReason);
fieldsByName.put("MDEntryOriginator",MDEntryOriginator);
fieldsByName.put("LocationID",LocationID);
fieldsByName.put("DeskID",DeskID);
fieldsByName.put("DeleteReason",DeleteReason);
fieldsByName.put("OpenCloseSettlFlag",OpenCloseSettlFlag);
fieldsByName.put("SellerDays",SellerDays);
fieldsByName.put("MDEntryBuyer",MDEntryBuyer);
fieldsByName.put("MDEntrySeller",MDEntrySeller);
fieldsByName.put("MDEntryPositionNo",MDEntryPositionNo);
fieldsByName.put("FinancialStatus",FinancialStatus);
fieldsByName.put("CorporateAction",CorporateAction);
fieldsByName.put("DefBidSize",DefBidSize);
fieldsByName.put("DefOfferSize",DefOfferSize);
fieldsByName.put("NoQuoteEntries",NoQuoteEntries);
fieldsByName.put("NoQuoteSets",NoQuoteSets);
fieldsByName.put("QuoteStatus",QuoteStatus);
fieldsByName.put("QuoteCancelType",QuoteCancelType);
fieldsByName.put("QuoteEntryID",QuoteEntryID);
fieldsByName.put("QuoteRejectReason",QuoteRejectReason);
fieldsByName.put("QuoteResponseLevel",QuoteResponseLevel);
fieldsByName.put("QuoteSetID",QuoteSetID);
fieldsByName.put("QuoteRequestType",QuoteRequestType);
fieldsByName.put("TotNoQuoteEntries",TotNoQuoteEntries);
fieldsByName.put("UnderlyingSecurityIDSource",UnderlyingSecurityIDSource);
fieldsByName.put("UnderlyingIssuer",UnderlyingIssuer);
fieldsByName.put("UnderlyingSecurityDesc",UnderlyingSecurityDesc);
fieldsByName.put("UnderlyingSecurityExchange",UnderlyingSecurityExchange);
fieldsByName.put("UnderlyingSecurityID",UnderlyingSecurityID);
fieldsByName.put("UnderlyingSecurityType",UnderlyingSecurityType);
fieldsByName.put("UnderlyingSymbol",UnderlyingSymbol);
fieldsByName.put("UnderlyingSymbolSfx",UnderlyingSymbolSfx);
fieldsByName.put("UnderlyingMaturityMonthYear",UnderlyingMaturityMonthYear);
fieldsByName.put("null",null);
fieldsByName.put("UnderlyingPutOrCall",UnderlyingPutOrCall);
fieldsByName.put("UnderlyingStrikePrice",UnderlyingStrikePrice);
fieldsByName.put("UnderlyingOptAttribute",UnderlyingOptAttribute);
fieldsByName.put("UnderlyingCurrency",UnderlyingCurrency);
fieldsByName.put("null",null);
fieldsByName.put("SecurityReqID",SecurityReqID);
fieldsByName.put("SecurityRequestType",SecurityRequestType);
fieldsByName.put("SecurityResponseID",SecurityResponseID);
fieldsByName.put("SecurityResponseType",SecurityResponseType);
fieldsByName.put("SecurityStatusReqID",SecurityStatusReqID);
fieldsByName.put("UnsolicitedIndicator",UnsolicitedIndicator);
fieldsByName.put("SecurityTradingStatus",SecurityTradingStatus);
fieldsByName.put("HaltReason",HaltReason);
fieldsByName.put("InViewOfCommon",InViewOfCommon);
fieldsByName.put("DueToRelated",DueToRelated);
fieldsByName.put("BuyVolume",BuyVolume);
fieldsByName.put("SellVolume",SellVolume);
fieldsByName.put("HighPx",HighPx);
fieldsByName.put("LowPx",LowPx);
fieldsByName.put("Adjustment",Adjustment);
fieldsByName.put("TradSesReqID",TradSesReqID);
fieldsByName.put("TradingSessionID",TradingSessionID);
fieldsByName.put("ContraTrader",ContraTrader);
fieldsByName.put("TradSesMethod",TradSesMethod);
fieldsByName.put("TradSesMode",TradSesMode);
fieldsByName.put("TradSesStatus",TradSesStatus);
fieldsByName.put("TradSesStartTime",TradSesStartTime);
fieldsByName.put("TradSesOpenTime",TradSesOpenTime);
fieldsByName.put("TradSesPreCloseTime",TradSesPreCloseTime);
fieldsByName.put("TradSesCloseTime",TradSesCloseTime);
fieldsByName.put("TradSesEndTime",TradSesEndTime);
fieldsByName.put("NumberOfOrders",NumberOfOrders);
fieldsByName.put("MessageEncoding",MessageEncoding);
fieldsByName.put("EncodedIssuerLen",EncodedIssuerLen);
fieldsByName.put("EncodedIssuer",EncodedIssuer);
fieldsByName.put("EncodedSecurityDescLen",EncodedSecurityDescLen);
fieldsByName.put("EncodedSecurityDesc",EncodedSecurityDesc);
fieldsByName.put("EncodedListExecInstLen",EncodedListExecInstLen);
fieldsByName.put("EncodedListExecInst",EncodedListExecInst);
fieldsByName.put("EncodedTextLen",EncodedTextLen);
fieldsByName.put("EncodedText",EncodedText);
fieldsByName.put("EncodedSubjectLen",EncodedSubjectLen);
fieldsByName.put("EncodedSubject",EncodedSubject);
fieldsByName.put("EncodedHeadlineLen",EncodedHeadlineLen);
fieldsByName.put("EncodedHeadline",EncodedHeadline);
fieldsByName.put("EncodedAllocTextLen",EncodedAllocTextLen);
fieldsByName.put("EncodedAllocText",EncodedAllocText);
fieldsByName.put("EncodedUnderlyingIssuerLen",EncodedUnderlyingIssuerLen);
fieldsByName.put("EncodedUnderlyingIssuer",EncodedUnderlyingIssuer);
fieldsByName.put("EncodedUnderlyingSecurityDescLen",EncodedUnderlyingSecurityDescLen);
fieldsByName.put("EncodedUnderlyingSecurityDesc",EncodedUnderlyingSecurityDesc);
fieldsByName.put("AllocPrice",AllocPrice);
fieldsByName.put("QuoteSetValidUntilTime",QuoteSetValidUntilTime);
fieldsByName.put("QuoteEntryRejectReason",QuoteEntryRejectReason);
fieldsByName.put("LastMsgSeqNumProcessed",LastMsgSeqNumProcessed);
fieldsByName.put("null",null);
fieldsByName.put("RefTagID",RefTagID);
fieldsByName.put("RefMsgType",RefMsgType);
fieldsByName.put("SessionRejectReason",SessionRejectReason);
fieldsByName.put("BidRequestTransType",BidRequestTransType);
fieldsByName.put("ContraBroker",ContraBroker);
fieldsByName.put("ComplianceID",ComplianceID);
fieldsByName.put("SolicitedFlag",SolicitedFlag);
fieldsByName.put("ExecRestatementReason",ExecRestatementReason);
fieldsByName.put("BusinessRejectRefID",BusinessRejectRefID);
fieldsByName.put("BusinessRejectReason",BusinessRejectReason);
fieldsByName.put("GrossTradeAmt",GrossTradeAmt);
fieldsByName.put("NoContraBrokers",NoContraBrokers);
fieldsByName.put("MaxMessageSize",MaxMessageSize);
fieldsByName.put("NoMsgTypes",NoMsgTypes);
fieldsByName.put("MsgDirection",MsgDirection);
fieldsByName.put("NoTradingSessions",NoTradingSessions);
fieldsByName.put("TotalVolumeTraded",TotalVolumeTraded);
fieldsByName.put("DiscretionInst",DiscretionInst);
fieldsByName.put("DiscretionOffsetValue",DiscretionOffsetValue);
fieldsByName.put("BidID",BidID);
fieldsByName.put("ClientBidID",ClientBidID);
fieldsByName.put("ListName",ListName);
fieldsByName.put("TotNoRelatedSym",TotNoRelatedSym);
fieldsByName.put("BidType",BidType);
fieldsByName.put("NumTickets",NumTickets);
fieldsByName.put("SideValue1",SideValue1);
fieldsByName.put("SideValue2",SideValue2);
fieldsByName.put("NoBidDescriptors",NoBidDescriptors);
fieldsByName.put("BidDescriptorType",BidDescriptorType);
fieldsByName.put("BidDescriptor",BidDescriptor);
fieldsByName.put("SideValueInd",SideValueInd);
fieldsByName.put("LiquidityPctLow",LiquidityPctLow);
fieldsByName.put("LiquidityPctHigh",LiquidityPctHigh);
fieldsByName.put("LiquidityValue",LiquidityValue);
fieldsByName.put("EFPTrackingError",EFPTrackingError);
fieldsByName.put("FairValue",FairValue);
fieldsByName.put("OutsideIndexPct",OutsideIndexPct);
fieldsByName.put("ValueOfFutures",ValueOfFutures);
fieldsByName.put("LiquidityIndType",LiquidityIndType);
fieldsByName.put("WtAverageLiquidity",WtAverageLiquidity);
fieldsByName.put("ExchangeForPhysical",ExchangeForPhysical);
fieldsByName.put("OutMainCntryUIndex",OutMainCntryUIndex);
fieldsByName.put("CrossPercent",CrossPercent);
fieldsByName.put("ProgRptReqs",ProgRptReqs);
fieldsByName.put("ProgPeriodInterval",ProgPeriodInterval);
fieldsByName.put("IncTaxInd",IncTaxInd);
fieldsByName.put("NumBidders",NumBidders);
fieldsByName.put("BidTradeType",BidTradeType);
fieldsByName.put("BasisPxType",BasisPxType);
fieldsByName.put("NoBidComponents",NoBidComponents);
fieldsByName.put("Country",Country);
fieldsByName.put("TotNoStrikes",TotNoStrikes);
fieldsByName.put("PriceType",PriceType);
fieldsByName.put("DayOrderQty",DayOrderQty);
fieldsByName.put("DayCumQty",DayCumQty);
fieldsByName.put("DayAvgPx",DayAvgPx);
fieldsByName.put("GTBookingInst",GTBookingInst);
fieldsByName.put("NoStrikes",NoStrikes);
fieldsByName.put("ListStatusType",ListStatusType);
fieldsByName.put("NetGrossInd",NetGrossInd);
fieldsByName.put("ListOrderStatus",ListOrderStatus);
fieldsByName.put("ExpireDate",ExpireDate);
fieldsByName.put("ListExecInstType",ListExecInstType);
fieldsByName.put("CxlRejResponseTo",CxlRejResponseTo);
fieldsByName.put("UnderlyingCouponRate",UnderlyingCouponRate);
fieldsByName.put("UnderlyingContractMultiplier",UnderlyingContractMultiplier);
fieldsByName.put("ContraTradeQty",ContraTradeQty);
fieldsByName.put("ContraTradeTime",ContraTradeTime);
fieldsByName.put("null",null);
fieldsByName.put("null",null);
fieldsByName.put("LiquidityNumSecurities",LiquidityNumSecurities);
fieldsByName.put("MultiLegReportingType",MultiLegReportingType);
fieldsByName.put("StrikeTime",StrikeTime);
fieldsByName.put("ListStatusText",ListStatusText);
fieldsByName.put("EncodedListStatusTextLen",EncodedListStatusTextLen);
fieldsByName.put("EncodedListStatusText",EncodedListStatusText);
fieldsByName.put("PartyIDSource",PartyIDSource);
fieldsByName.put("PartyID",PartyID);
fieldsByName.put("null",null);
fieldsByName.put("null",null);
fieldsByName.put("NetChgPrevDay",NetChgPrevDay);
fieldsByName.put("PartyRole",PartyRole);
fieldsByName.put("NoPartyIDs",NoPartyIDs);
fieldsByName.put("NoSecurityAltID",NoSecurityAltID);
fieldsByName.put("SecurityAltID",SecurityAltID);
fieldsByName.put("SecurityAltIDSource",SecurityAltIDSource);
fieldsByName.put("NoUnderlyingSecurityAltID",NoUnderlyingSecurityAltID);
fieldsByName.put("UnderlyingSecurityAltID",UnderlyingSecurityAltID);
fieldsByName.put("UnderlyingSecurityAltIDSource",UnderlyingSecurityAltIDSource);
fieldsByName.put("Product",Product);
fieldsByName.put("CFICode",CFICode);
fieldsByName.put("UnderlyingProduct",UnderlyingProduct);
fieldsByName.put("UnderlyingCFICode",UnderlyingCFICode);
fieldsByName.put("TestMessageIndicator",TestMessageIndicator);
fieldsByName.put("QuantityType",QuantityType);
fieldsByName.put("BookingRefID",BookingRefID);
fieldsByName.put("IndividualAllocID",IndividualAllocID);
fieldsByName.put("RoundingDirection",RoundingDirection);
fieldsByName.put("RoundingModulus",RoundingModulus);
fieldsByName.put("CountryOfIssue",CountryOfIssue);
fieldsByName.put("StateOrProvinceOfIssue",StateOrProvinceOfIssue);
fieldsByName.put("LocaleOfIssue",LocaleOfIssue);
fieldsByName.put("NoRegistDtls",NoRegistDtls);
fieldsByName.put("MailingDtls",MailingDtls);
fieldsByName.put("InvestorCountryOfResidence",InvestorCountryOfResidence);
fieldsByName.put("PaymentRef",PaymentRef);
fieldsByName.put("DistribPaymentMethod",DistribPaymentMethod);
fieldsByName.put("CashDistribCurr",CashDistribCurr);
fieldsByName.put("CommCurrency",CommCurrency);
fieldsByName.put("CancellationRights",CancellationRights);
fieldsByName.put("MoneyLaunderingStatus",MoneyLaunderingStatus);
fieldsByName.put("MailingInst",MailingInst);
fieldsByName.put("TransBkdTime",TransBkdTime);
fieldsByName.put("ExecPriceType",ExecPriceType);
fieldsByName.put("ExecPriceAdjustment",ExecPriceAdjustment);
fieldsByName.put("DateOfBirth",DateOfBirth);
fieldsByName.put("TradeReportTransType",TradeReportTransType);
fieldsByName.put("CardHolderName",CardHolderName);
fieldsByName.put("CardNumber",CardNumber);
fieldsByName.put("CardExpDate",CardExpDate);
fieldsByName.put("CardIssNum",CardIssNum);
fieldsByName.put("PaymentMethod",PaymentMethod);
fieldsByName.put("RegistAcctType",RegistAcctType);
fieldsByName.put("Designation",Designation);
fieldsByName.put("TaxAdvantageType",TaxAdvantageType);
fieldsByName.put("RegistRejReasonText",RegistRejReasonText);
fieldsByName.put("FundRenewWaiv",FundRenewWaiv);
fieldsByName.put("CashDistribAgentName",CashDistribAgentName);
fieldsByName.put("CashDistribAgentCode",CashDistribAgentCode);
fieldsByName.put("CashDistribAgentAcctNumber",CashDistribAgentAcctNumber);
fieldsByName.put("CashDistribPayRef",CashDistribPayRef);
fieldsByName.put("CashDistribAgentAcctName",CashDistribAgentAcctName);
fieldsByName.put("CardStartDate",CardStartDate);
fieldsByName.put("PaymentDate",PaymentDate);
fieldsByName.put("PaymentRemitterID",PaymentRemitterID);
fieldsByName.put("RegistStatus",RegistStatus);
fieldsByName.put("RegistRejReasonCode",RegistRejReasonCode);
fieldsByName.put("RegistRefID",RegistRefID);
fieldsByName.put("RegistDtls",RegistDtls);
fieldsByName.put("NoDistribInsts",NoDistribInsts);
fieldsByName.put("RegistEmail",RegistEmail);
fieldsByName.put("DistribPercentage",DistribPercentage);
fieldsByName.put("RegistID",RegistID);
fieldsByName.put("RegistTransType",RegistTransType);
fieldsByName.put("ExecValuationPoint",ExecValuationPoint);
fieldsByName.put("OrderPercent",OrderPercent);
fieldsByName.put("OwnershipType",OwnershipType);
fieldsByName.put("NoContAmts",NoContAmts);
fieldsByName.put("ContAmtType",ContAmtType);
fieldsByName.put("ContAmtValue",ContAmtValue);
fieldsByName.put("ContAmtCurr",ContAmtCurr);
fieldsByName.put("OwnerType",OwnerType);
fieldsByName.put("PartySubID",PartySubID);
fieldsByName.put("NestedPartyID",NestedPartyID);
fieldsByName.put("NestedPartyIDSource",NestedPartyIDSource);
fieldsByName.put("SecondaryClOrdID",SecondaryClOrdID);
fieldsByName.put("SecondaryExecID",SecondaryExecID);
fieldsByName.put("OrderCapacity",OrderCapacity);
fieldsByName.put("OrderRestrictions",OrderRestrictions);
fieldsByName.put("MassCancelRequestType",MassCancelRequestType);
fieldsByName.put("MassCancelResponse",MassCancelResponse);
fieldsByName.put("MassCancelRejectReason",MassCancelRejectReason);
fieldsByName.put("TotalAffectedOrders",TotalAffectedOrders);
fieldsByName.put("NoAffectedOrders",NoAffectedOrders);
fieldsByName.put("AffectedOrderID",AffectedOrderID);
fieldsByName.put("AffectedSecondaryOrderID",AffectedSecondaryOrderID);
fieldsByName.put("QuoteType",QuoteType);
fieldsByName.put("NestedPartyRole",NestedPartyRole);
fieldsByName.put("NoNestedPartyIDs",NoNestedPartyIDs);
fieldsByName.put("TotalAccruedInterestAmt",TotalAccruedInterestAmt);
fieldsByName.put("MaturityDate",MaturityDate);
fieldsByName.put("UnderlyingMaturityDate",UnderlyingMaturityDate);
fieldsByName.put("InstrRegistry",InstrRegistry);
fieldsByName.put("CashMargin",CashMargin);
fieldsByName.put("NestedPartySubID",NestedPartySubID);
fieldsByName.put("Scope",Scope);
fieldsByName.put("MDImplicitDelete",MDImplicitDelete);
fieldsByName.put("CrossID",CrossID);
fieldsByName.put("CrossType",CrossType);
fieldsByName.put("CrossPrioritization",CrossPrioritization);
fieldsByName.put("OrigCrossID",OrigCrossID);
fieldsByName.put("NoSides",NoSides);
fieldsByName.put("Username",Username);
fieldsByName.put("Password",Password);
fieldsByName.put("NoLegs",NoLegs);
fieldsByName.put("LegCurrency",LegCurrency);
fieldsByName.put("TotNoSecurityTypes",TotNoSecurityTypes);
fieldsByName.put("NoSecurityTypes",NoSecurityTypes);
fieldsByName.put("SecurityListRequestType",SecurityListRequestType);
fieldsByName.put("SecurityRequestResult",SecurityRequestResult);
fieldsByName.put("RoundLot",RoundLot);
fieldsByName.put("MinTradeVol",MinTradeVol);
fieldsByName.put("MultiLegRptTypeReq",MultiLegRptTypeReq);
fieldsByName.put("LegPositionEffect",LegPositionEffect);
fieldsByName.put("LegCoveredOrUncovered",LegCoveredOrUncovered);
fieldsByName.put("LegPrice",LegPrice);
fieldsByName.put("TradSesStatusRejReason",TradSesStatusRejReason);
fieldsByName.put("TradeRequestID",TradeRequestID);
fieldsByName.put("TradeRequestType",TradeRequestType);
fieldsByName.put("PreviouslyReported",PreviouslyReported);
fieldsByName.put("TradeReportID",TradeReportID);
fieldsByName.put("TradeReportRefID",TradeReportRefID);
fieldsByName.put("MatchStatus",MatchStatus);
fieldsByName.put("MatchType",MatchType);
fieldsByName.put("OddLot",OddLot);
fieldsByName.put("NoClearingInstructions",NoClearingInstructions);
fieldsByName.put("ClearingInstruction",ClearingInstruction);
fieldsByName.put("TradeInputSource",TradeInputSource);
fieldsByName.put("TradeInputDevice",TradeInputDevice);
fieldsByName.put("NoDates",NoDates);
fieldsByName.put("AccountType",AccountType);
fieldsByName.put("CustOrderCapacity",CustOrderCapacity);
fieldsByName.put("ClOrdLinkID",ClOrdLinkID);
fieldsByName.put("MassStatusReqID",MassStatusReqID);
fieldsByName.put("MassStatusReqType",MassStatusReqType);
fieldsByName.put("OrigOrdModTime",OrigOrdModTime);
fieldsByName.put("LegSettlType",LegSettlType);
fieldsByName.put("LegSettlDate",LegSettlDate);
fieldsByName.put("DayBookingInst",DayBookingInst);
fieldsByName.put("BookingUnit",BookingUnit);
fieldsByName.put("PreallocMethod",PreallocMethod);
fieldsByName.put("UnderlyingCountryOfIssue",UnderlyingCountryOfIssue);
fieldsByName.put("UnderlyingStateOrProvinceOfIssue",UnderlyingStateOrProvinceOfIssue);
fieldsByName.put("UnderlyingLocaleOfIssue",UnderlyingLocaleOfIssue);
fieldsByName.put("UnderlyingInstrRegistry",UnderlyingInstrRegistry);
fieldsByName.put("LegCountryOfIssue",LegCountryOfIssue);
fieldsByName.put("LegStateOrProvinceOfIssue",LegStateOrProvinceOfIssue);
fieldsByName.put("LegLocaleOfIssue",LegLocaleOfIssue);
fieldsByName.put("LegInstrRegistry",LegInstrRegistry);
fieldsByName.put("LegSymbol",LegSymbol);
fieldsByName.put("LegSymbolSfx",LegSymbolSfx);
fieldsByName.put("LegSecurityID",LegSecurityID);
fieldsByName.put("LegSecurityIDSource",LegSecurityIDSource);
fieldsByName.put("NoLegSecurityAltID",NoLegSecurityAltID);
fieldsByName.put("LegSecurityAltID",LegSecurityAltID);
fieldsByName.put("LegSecurityAltIDSource",LegSecurityAltIDSource);
fieldsByName.put("LegProduct",LegProduct);
fieldsByName.put("LegCFICode",LegCFICode);
fieldsByName.put("LegSecurityType",LegSecurityType);
fieldsByName.put("LegMaturityMonthYear",LegMaturityMonthYear);
fieldsByName.put("LegMaturityDate",LegMaturityDate);
fieldsByName.put("LegStrikePrice",LegStrikePrice);
fieldsByName.put("LegOptAttribute",LegOptAttribute);
fieldsByName.put("LegContractMultiplier",LegContractMultiplier);
fieldsByName.put("LegCouponRate",LegCouponRate);
fieldsByName.put("LegSecurityExchange",LegSecurityExchange);
fieldsByName.put("LegIssuer",LegIssuer);
fieldsByName.put("EncodedLegIssuerLen",EncodedLegIssuerLen);
fieldsByName.put("EncodedLegIssuer",EncodedLegIssuer);
fieldsByName.put("LegSecurityDesc",LegSecurityDesc);
fieldsByName.put("EncodedLegSecurityDescLen",EncodedLegSecurityDescLen);
fieldsByName.put("EncodedLegSecurityDesc",EncodedLegSecurityDesc);
fieldsByName.put("LegRatioQty",LegRatioQty);
fieldsByName.put("LegSide",LegSide);
fieldsByName.put("TradingSessionSubID",TradingSessionSubID);
fieldsByName.put("AllocType",AllocType);
fieldsByName.put("NoHops",NoHops);
fieldsByName.put("HopCompID",HopCompID);
fieldsByName.put("HopSendingTime",HopSendingTime);
fieldsByName.put("HopRefID",HopRefID);
fieldsByName.put("MidPx",MidPx);
fieldsByName.put("BidYield",BidYield);
fieldsByName.put("MidYield",MidYield);
fieldsByName.put("OfferYield",OfferYield);
fieldsByName.put("ClearingFeeIndicator",ClearingFeeIndicator);
fieldsByName.put("WorkingIndicator",WorkingIndicator);
fieldsByName.put("LegLastPx",LegLastPx);
fieldsByName.put("PriorityIndicator",PriorityIndicator);
fieldsByName.put("PriceImprovement",PriceImprovement);
fieldsByName.put("Price2",Price2);
fieldsByName.put("LastForwardPoints2",LastForwardPoints2);
fieldsByName.put("BidForwardPoints2",BidForwardPoints2);
fieldsByName.put("OfferForwardPoints2",OfferForwardPoints2);
fieldsByName.put("RFQReqID",RFQReqID);
fieldsByName.put("MktBidPx",MktBidPx);
fieldsByName.put("MktOfferPx",MktOfferPx);
fieldsByName.put("MinBidSize",MinBidSize);
fieldsByName.put("MinOfferSize",MinOfferSize);
fieldsByName.put("QuoteStatusReqID",QuoteStatusReqID);
fieldsByName.put("LegalConfirm",LegalConfirm);
fieldsByName.put("UnderlyingLastPx",UnderlyingLastPx);
fieldsByName.put("UnderlyingLastQty",UnderlyingLastQty);
fieldsByName.put("null",null);
fieldsByName.put("LegRefID",LegRefID);
fieldsByName.put("ContraLegRefID",ContraLegRefID);
fieldsByName.put("SettlCurrBidFxRate",SettlCurrBidFxRate);
fieldsByName.put("SettlCurrOfferFxRate",SettlCurrOfferFxRate);
fieldsByName.put("QuoteRequestRejectReason",QuoteRequestRejectReason);
fieldsByName.put("SideComplianceID",SideComplianceID);
fieldsByName.put("AcctIDSource",AcctIDSource);
fieldsByName.put("AllocAcctIDSource",AllocAcctIDSource);
fieldsByName.put("BenchmarkPrice",BenchmarkPrice);
fieldsByName.put("BenchmarkPriceType",BenchmarkPriceType);
fieldsByName.put("ConfirmID",ConfirmID);
fieldsByName.put("ConfirmStatus",ConfirmStatus);
fieldsByName.put("ConfirmTransType",ConfirmTransType);
fieldsByName.put("ContractSettlMonth",ContractSettlMonth);
fieldsByName.put("DeliveryForm",DeliveryForm);
fieldsByName.put("LastParPx",LastParPx);
fieldsByName.put("NoLegAllocs",NoLegAllocs);
fieldsByName.put("LegAllocAccount",LegAllocAccount);
fieldsByName.put("LegIndividualAllocID",LegIndividualAllocID);
fieldsByName.put("LegAllocQty",LegAllocQty);
fieldsByName.put("LegAllocAcctIDSource",LegAllocAcctIDSource);
fieldsByName.put("LegSettlCurrency",LegSettlCurrency);
fieldsByName.put("LegBenchmarkCurveCurrency",LegBenchmarkCurveCurrency);
fieldsByName.put("LegBenchmarkCurveName",LegBenchmarkCurveName);
fieldsByName.put("LegBenchmarkCurvePoint",LegBenchmarkCurvePoint);
fieldsByName.put("LegBenchmarkPrice",LegBenchmarkPrice);
fieldsByName.put("LegBenchmarkPriceType",LegBenchmarkPriceType);
fieldsByName.put("LegBidPx",LegBidPx);
fieldsByName.put("LegIOIQty",LegIOIQty);
fieldsByName.put("NoLegStipulations",NoLegStipulations);
fieldsByName.put("LegOfferPx",LegOfferPx);
fieldsByName.put("LegOrderQty",LegOrderQty);
fieldsByName.put("LegPriceType",LegPriceType);
fieldsByName.put("LegQty",LegQty);
fieldsByName.put("LegStipulationType",LegStipulationType);
fieldsByName.put("LegStipulationValue",LegStipulationValue);
fieldsByName.put("LegSwapType",LegSwapType);
fieldsByName.put("Pool",Pool);
fieldsByName.put("QuotePriceType",QuotePriceType);
fieldsByName.put("QuoteRespID",QuoteRespID);
fieldsByName.put("QuoteRespType",QuoteRespType);
fieldsByName.put("QuoteQualifier",QuoteQualifier);
fieldsByName.put("YieldRedemptionDate",YieldRedemptionDate);
fieldsByName.put("YieldRedemptionPrice",YieldRedemptionPrice);
fieldsByName.put("YieldRedemptionPriceType",YieldRedemptionPriceType);
fieldsByName.put("BenchmarkSecurityID",BenchmarkSecurityID);
fieldsByName.put("ReversalIndicator",ReversalIndicator);
fieldsByName.put("YieldCalcDate",YieldCalcDate);
fieldsByName.put("NoPositions",NoPositions);
fieldsByName.put("PosType",PosType);
fieldsByName.put("LongQty",LongQty);
fieldsByName.put("ShortQty",ShortQty);
fieldsByName.put("PosQtyStatus",PosQtyStatus);
fieldsByName.put("PosAmtType",PosAmtType);
fieldsByName.put("PosAmt",PosAmt);
fieldsByName.put("PosTransType",PosTransType);
fieldsByName.put("PosReqID",PosReqID);
fieldsByName.put("NoUnderlyings",NoUnderlyings);
fieldsByName.put("PosMaintAction",PosMaintAction);
fieldsByName.put("OrigPosReqRefID",OrigPosReqRefID);
fieldsByName.put("PosMaintRptRefID",PosMaintRptRefID);
fieldsByName.put("ClearingBusinessDate",ClearingBusinessDate);
fieldsByName.put("SettlSessID",SettlSessID);
fieldsByName.put("SettlSessSubID",SettlSessSubID);
fieldsByName.put("AdjustmentType",AdjustmentType);
fieldsByName.put("ContraryInstructionIndicator",ContraryInstructionIndicator);
fieldsByName.put("PriorSpreadIndicator",PriorSpreadIndicator);
fieldsByName.put("PosMaintRptID",PosMaintRptID);
fieldsByName.put("PosMaintStatus",PosMaintStatus);
fieldsByName.put("PosMaintResult",PosMaintResult);
fieldsByName.put("PosReqType",PosReqType);
fieldsByName.put("ResponseTransportType",ResponseTransportType);
fieldsByName.put("ResponseDestination",ResponseDestination);
fieldsByName.put("TotalNumPosReports",TotalNumPosReports);
fieldsByName.put("PosReqResult",PosReqResult);
fieldsByName.put("PosReqStatus",PosReqStatus);
fieldsByName.put("SettlPrice",SettlPrice);
fieldsByName.put("SettlPriceType",SettlPriceType);
fieldsByName.put("UnderlyingSettlPrice",UnderlyingSettlPrice);
fieldsByName.put("UnderlyingSettlPriceType",UnderlyingSettlPriceType);
fieldsByName.put("PriorSettlPrice",PriorSettlPrice);
fieldsByName.put("NoQuoteQualifiers",NoQuoteQualifiers);
fieldsByName.put("AllocSettlCurrency",AllocSettlCurrency);
fieldsByName.put("AllocSettlCurrAmt",AllocSettlCurrAmt);
fieldsByName.put("InterestAtMaturity",InterestAtMaturity);
fieldsByName.put("LegDatedDate",LegDatedDate);
fieldsByName.put("LegPool",LegPool);
fieldsByName.put("AllocInterestAtMaturity",AllocInterestAtMaturity);
fieldsByName.put("AllocAccruedInterestAmt",AllocAccruedInterestAmt);
fieldsByName.put("DeliveryDate",DeliveryDate);
fieldsByName.put("AssignmentMethod",AssignmentMethod);
fieldsByName.put("AssignmentUnit",AssignmentUnit);
fieldsByName.put("OpenInterest",OpenInterest);
fieldsByName.put("ExerciseMethod",ExerciseMethod);
fieldsByName.put("TotNumTradeReports",TotNumTradeReports);
fieldsByName.put("TradeRequestResult",TradeRequestResult);
fieldsByName.put("TradeRequestStatus",TradeRequestStatus);
fieldsByName.put("TradeReportRejectReason",TradeReportRejectReason);
fieldsByName.put("SideMultiLegReportingType",SideMultiLegReportingType);
fieldsByName.put("NoPosAmt",NoPosAmt);
fieldsByName.put("AutoAcceptIndicator",AutoAcceptIndicator);
fieldsByName.put("AllocReportID",AllocReportID);
fieldsByName.put("NoNested2PartyIDs",NoNested2PartyIDs);
fieldsByName.put("Nested2PartyID",Nested2PartyID);
fieldsByName.put("Nested2PartyIDSource",Nested2PartyIDSource);
fieldsByName.put("Nested2PartyRole",Nested2PartyRole);
fieldsByName.put("Nested2PartySubID",Nested2PartySubID);
fieldsByName.put("BenchmarkSecurityIDSource",BenchmarkSecurityIDSource);
fieldsByName.put("SecuritySubType",SecuritySubType);
fieldsByName.put("UnderlyingSecuritySubType",UnderlyingSecuritySubType);
fieldsByName.put("LegSecuritySubType",LegSecuritySubType);
fieldsByName.put("AllowableOneSidednessPct",AllowableOneSidednessPct);
fieldsByName.put("AllowableOneSidednessValue",AllowableOneSidednessValue);
fieldsByName.put("AllowableOneSidednessCurr",AllowableOneSidednessCurr);
fieldsByName.put("NoTrdRegTimestamps",NoTrdRegTimestamps);
fieldsByName.put("TrdRegTimestamp",TrdRegTimestamp);
fieldsByName.put("TrdRegTimestampType",TrdRegTimestampType);
fieldsByName.put("TrdRegTimestampOrigin",TrdRegTimestampOrigin);
fieldsByName.put("ConfirmRefID",ConfirmRefID);
fieldsByName.put("ConfirmType",ConfirmType);
fieldsByName.put("ConfirmRejReason",ConfirmRejReason);
fieldsByName.put("BookingType",BookingType);
fieldsByName.put("IndividualAllocRejCode",IndividualAllocRejCode);
fieldsByName.put("SettlInstMsgID",SettlInstMsgID);
fieldsByName.put("NoSettlInst",NoSettlInst);
fieldsByName.put("LastUpdateTime",LastUpdateTime);
fieldsByName.put("AllocSettlInstType",AllocSettlInstType);
fieldsByName.put("NoSettlPartyIDs",NoSettlPartyIDs);
fieldsByName.put("SettlPartyID",SettlPartyID);
fieldsByName.put("SettlPartyIDSource",SettlPartyIDSource);
fieldsByName.put("SettlPartyRole",SettlPartyRole);
fieldsByName.put("SettlPartySubID",SettlPartySubID);
fieldsByName.put("SettlPartySubIDType",SettlPartySubIDType);
fieldsByName.put("DlvyInstType",DlvyInstType);
fieldsByName.put("TerminationType",TerminationType);
fieldsByName.put("NextExpectedMsgSeqNum",NextExpectedMsgSeqNum);
fieldsByName.put("OrdStatusReqID",OrdStatusReqID);
fieldsByName.put("SettlInstReqID",SettlInstReqID);
fieldsByName.put("SettlInstReqRejCode",SettlInstReqRejCode);
fieldsByName.put("SecondaryAllocID",SecondaryAllocID);
fieldsByName.put("AllocReportType",AllocReportType);
fieldsByName.put("AllocReportRefID",AllocReportRefID);
fieldsByName.put("AllocCancReplaceReason",AllocCancReplaceReason);
fieldsByName.put("CopyMsgIndicator",CopyMsgIndicator);
fieldsByName.put("AllocAccountType",AllocAccountType);
fieldsByName.put("OrderAvgPx",OrderAvgPx);
fieldsByName.put("OrderBookingQty",OrderBookingQty);
fieldsByName.put("NoSettlPartySubIDs",NoSettlPartySubIDs);
fieldsByName.put("NoPartySubIDs",NoPartySubIDs);
fieldsByName.put("PartySubIDType",PartySubIDType);
fieldsByName.put("NoNestedPartySubIDs",NoNestedPartySubIDs);
fieldsByName.put("NestedPartySubIDType",NestedPartySubIDType);
fieldsByName.put("NoNested2PartySubIDs",NoNested2PartySubIDs);
fieldsByName.put("Nested2PartySubIDType",Nested2PartySubIDType);
fieldsByName.put("AllocIntermedReqType",AllocIntermedReqType);
fieldsByName.put("null",null);
fieldsByName.put("UnderlyingPx",UnderlyingPx);
fieldsByName.put("PriceDelta",PriceDelta);
fieldsByName.put("ApplQueueMax",ApplQueueMax);
fieldsByName.put("ApplQueueDepth",ApplQueueDepth);
fieldsByName.put("ApplQueueResolution",ApplQueueResolution);
fieldsByName.put("ApplQueueAction",ApplQueueAction);
fieldsByName.put("NoAltMDSource",NoAltMDSource);
fieldsByName.put("AltMDSourceID",AltMDSourceID);
fieldsByName.put("SecondaryTradeReportID",SecondaryTradeReportID);
fieldsByName.put("AvgPxIndicator",AvgPxIndicator);
fieldsByName.put("TradeLinkID",TradeLinkID);
fieldsByName.put("OrderInputDevice",OrderInputDevice);
fieldsByName.put("UnderlyingTradingSessionID",UnderlyingTradingSessionID);
fieldsByName.put("UnderlyingTradingSessionSubID",UnderlyingTradingSessionSubID);
fieldsByName.put("TradeLegRefID",TradeLegRefID);
fieldsByName.put("ExchangeRule",ExchangeRule);
fieldsByName.put("TradeAllocIndicator",TradeAllocIndicator);
fieldsByName.put("ExpirationCycle",ExpirationCycle);
fieldsByName.put("TrdType",TrdType);
fieldsByName.put("TrdSubType",TrdSubType);
fieldsByName.put("TransferReason",TransferReason);
fieldsByName.put("AsgnReqID",AsgnReqID);
fieldsByName.put("TotNumAssignmentReports",TotNumAssignmentReports);
fieldsByName.put("AsgnRptID",AsgnRptID);
fieldsByName.put("ThresholdAmount",ThresholdAmount);
fieldsByName.put("PegMoveType",PegMoveType);
fieldsByName.put("PegOffsetType",PegOffsetType);
fieldsByName.put("PegLimitType",PegLimitType);
fieldsByName.put("PegRoundDirection",PegRoundDirection);
fieldsByName.put("PeggedPrice",PeggedPrice);
fieldsByName.put("PegScope",PegScope);
fieldsByName.put("DiscretionMoveType",DiscretionMoveType);
fieldsByName.put("DiscretionOffsetType",DiscretionOffsetType);
fieldsByName.put("DiscretionLimitType",DiscretionLimitType);
fieldsByName.put("DiscretionRoundDirection",DiscretionRoundDirection);
fieldsByName.put("DiscretionPrice",DiscretionPrice);
fieldsByName.put("DiscretionScope",DiscretionScope);
fieldsByName.put("TargetStrategy",TargetStrategy);
fieldsByName.put("TargetStrategyParameters",TargetStrategyParameters);
fieldsByName.put("ParticipationRate",ParticipationRate);
fieldsByName.put("TargetStrategyPerformance",TargetStrategyPerformance);
fieldsByName.put("LastLiquidityInd",LastLiquidityInd);
fieldsByName.put("PublishTrdIndicator",PublishTrdIndicator);
fieldsByName.put("ShortSaleReason",ShortSaleReason);
fieldsByName.put("QtyType",QtyType);
fieldsByName.put("SecondaryTrdType",SecondaryTrdType);
fieldsByName.put("TradeReportType",TradeReportType);
fieldsByName.put("AllocNoOrdersType",AllocNoOrdersType);
fieldsByName.put("SharedCommission",SharedCommission);
fieldsByName.put("ConfirmReqID",ConfirmReqID);
fieldsByName.put("AvgParPx",AvgParPx);
fieldsByName.put("ReportedPx",ReportedPx);
fieldsByName.put("NoCapacities",NoCapacities);
fieldsByName.put("OrderCapacityQty",OrderCapacityQty);
fieldsByName.put("NoEvents",NoEvents);
fieldsByName.put("EventType",EventType);
fieldsByName.put("EventDate",EventDate);
fieldsByName.put("EventPx",EventPx);
fieldsByName.put("EventText",EventText);
fieldsByName.put("PctAtRisk",PctAtRisk);
fieldsByName.put("NoInstrAttrib",NoInstrAttrib);
fieldsByName.put("InstrAttribType",InstrAttribType);
fieldsByName.put("InstrAttribValue",InstrAttribValue);
fieldsByName.put("DatedDate",DatedDate);
fieldsByName.put("InterestAccrualDate",InterestAccrualDate);
fieldsByName.put("CPProgram",CPProgram);
fieldsByName.put("CPRegType",CPRegType);
fieldsByName.put("UnderlyingCPProgram",UnderlyingCPProgram);
fieldsByName.put("UnderlyingCPRegType",UnderlyingCPRegType);
fieldsByName.put("UnderlyingQty",UnderlyingQty);
fieldsByName.put("TrdMatchID",TrdMatchID);
fieldsByName.put("SecondaryTradeReportRefID",SecondaryTradeReportRefID);
fieldsByName.put("UnderlyingDirtyPrice",UnderlyingDirtyPrice);
fieldsByName.put("UnderlyingEndPrice",UnderlyingEndPrice);
fieldsByName.put("UnderlyingStartValue",UnderlyingStartValue);
fieldsByName.put("UnderlyingCurrentValue",UnderlyingCurrentValue);
fieldsByName.put("UnderlyingEndValue",UnderlyingEndValue);
fieldsByName.put("NoUnderlyingStips",NoUnderlyingStips);
fieldsByName.put("UnderlyingStipType",UnderlyingStipType);
fieldsByName.put("UnderlyingStipValue",UnderlyingStipValue);
fieldsByName.put("MaturityNetMoney",MaturityNetMoney);
fieldsByName.put("MiscFeeBasis",MiscFeeBasis);
fieldsByName.put("TotNoAllocs",TotNoAllocs);
fieldsByName.put("LastFragment",LastFragment);
fieldsByName.put("CollReqID",CollReqID);
fieldsByName.put("CollAsgnReason",CollAsgnReason);
fieldsByName.put("CollInquiryQualifier",CollInquiryQualifier);
fieldsByName.put("NoTrades",NoTrades);
fieldsByName.put("MarginRatio",MarginRatio);
fieldsByName.put("MarginExcess",MarginExcess);
fieldsByName.put("TotalNetValue",TotalNetValue);
fieldsByName.put("CashOutstanding",CashOutstanding);
fieldsByName.put("CollAsgnID",CollAsgnID);
fieldsByName.put("CollAsgnTransType",CollAsgnTransType);
fieldsByName.put("CollRespID",CollRespID);
fieldsByName.put("CollAsgnRespType",CollAsgnRespType);
fieldsByName.put("CollAsgnRejectReason",CollAsgnRejectReason);
fieldsByName.put("CollAsgnRefID",CollAsgnRefID);
fieldsByName.put("CollRptID",CollRptID);
fieldsByName.put("CollInquiryID",CollInquiryID);
fieldsByName.put("CollStatus",CollStatus);
fieldsByName.put("TotNumReports",TotNumReports);
fieldsByName.put("LastRptRequested",LastRptRequested);
fieldsByName.put("AgreementDesc",AgreementDesc);
fieldsByName.put("AgreementID",AgreementID);
fieldsByName.put("AgreementDate",AgreementDate);
}

}
