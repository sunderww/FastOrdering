//
//  DishCell.h
//  FastOrdering
//
//  Created by Sunder on 23/03/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Dish.h"

@class DishCell;

@protocol DishCellDelegate <UITextFieldDelegate>

@optional
- (void)editButtonClickedForDishCell:(DishCell *)cell;

@end


@interface DishCell : UITableViewCell {
	IBOutlet UILabel *      mainLabel;
	IBOutlet UIButton *		editButton;
}

- (void)setDish:(Dish *)dish andTag:(NSUInteger)tag;
- (void)setQuantity:(NSInteger)quantity;
- (void)setEditable:(BOOL)editable;

@property (nonatomic, strong) UITextField *         textField;
@property (nonatomic, retain) id<DishCellDelegate>	delegate;

- (IBAction)edit;

@end
