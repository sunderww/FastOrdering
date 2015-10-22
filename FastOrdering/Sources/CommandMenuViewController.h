//
//  CommandMenuViewController.h
//  FastOrdering
//
//  Created by Sunder on 23/03/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SLExpandableTableView.h"
#import "MenuComposition.h"
#import "DishCell.h"

@protocol CommandMenuDelegate <NSObject>

- (void)didOrderDishes:(NSArray *)dishes;
- (void)popCommandMenuView;

@end

@interface CommandMenuViewController : UIViewController <SLExpandableTableViewDatasource, SLExpandableTableViewDelegate, DishCellDelegate, UIScrollViewDelegate> {
	
	IBOutlet SLExpandableTableView *  expandableTableView;
	IBOutlet UIButton * orderButton;
	
	UITextField * responder;
	NSMutableArray * counts;
	NSArray * categories;
	NSArray * dishes;
}

- (IBAction)orderTaken;
- (IBAction)cancel;

@property (nonatomic, retain) MenuComposition *       composition;
@property (nonatomic, retain) id<CommandMenuDelegate> delegate;

@end
