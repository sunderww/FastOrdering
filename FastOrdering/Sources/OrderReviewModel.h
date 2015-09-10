//
//  OrderReviewModel.h
//  FastOrdering
//
//  Created by Sunder on 08/04/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SLExpandableTableView.h"
#import "Order+Custom.h"

@interface OrderReviewModel : NSObject <SLExpandableTableViewDatasource, SLExpandableTableViewDelegate, UITextFieldDelegate> {
	NSArray * sections;
	NSArray * dishes;
	
	UITextField * responder;
}

- (void)reloadData;

@property (nonatomic, strong) Order * order;
@property (nonatomic, strong) UITableView * tableView;

@end
