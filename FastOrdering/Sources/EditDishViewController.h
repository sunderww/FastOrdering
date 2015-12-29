//
//  EditDishViewController.h
//  FastOrdering
//
//  Created by Sunder on 06/10/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "OrderedDish.h"
#import "SLExpandableTableView.h"
#import "OptionCell.h"

@protocol EditDishDelegate <NSObject>

@required
- (void)popEditDishView;

@end

@interface EditDishViewController : UIViewController <SLExpandableTableViewDatasource, SLExpandableTableViewDelegate, UITextFieldDelegate, UITextViewDelegate> {
	IBOutlet UILabel *		commentLabel;
	IBOutlet UITextView *	commentView;
	IBOutlet UIButton *		validateButton;
	IBOutlet UIView *		commentSuperview;
	IBOutlet UIView *		commentPlaceholder;
	IBOutlet UIButton *		backButton;

	IBOutlet SLExpandableTableView *	expandableTableView;
	IBOutlet UITapGestureRecognizer *	gesture;
	
	NSArray * categories;
	NSArray * options;
	NSArray * allOptions;
	
	CGSize keyboardSize;
	UITextField * responder;
	BOOL saved;
	NSManagedObjectContext * context;
}

@property (nonatomic, retain) OrderedDish *			dish;
@property (nonatomic, strong) id<EditDishDelegate>	delegate;

- (IBAction)validate;
- (IBAction)cancel;
- (IBAction)endEditing;

@end
