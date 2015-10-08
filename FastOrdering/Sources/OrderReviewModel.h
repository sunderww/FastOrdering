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
#import "DishCell.h"


@protocol OrderReviewDelegate <UITextFieldDelegate>

@required
- (void)orderedDishClicked:(OrderedDish *)dish;

@end

@interface OrderReviewModel : NSObject <SLExpandableTableViewDatasource, SLExpandableTableViewDelegate, DishCellDelegate> {
	NSArray * sections;
	NSArray * dishes;
	
	UITextField * responder;
}

- (void)reloadData;

@property (nonatomic, strong) Order * order;
@property (nonatomic, strong) UITableView * tableView;
@property (nonatomic, strong) id<OrderReviewDelegate> delegate;

@end
