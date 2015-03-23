//
//  CommandViewController.m
//  FastOrdering
//
//  Created by Sunder on 04/03/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import "CommandViewController.h"
#import "CommandMenuViewController.h"

@interface CommandViewController ()

@end

@implementation CommandViewController

- (void)viewDidLoad {
  [super viewDidLoad];
  
  menuModel = [OrderMenuModel new];
  carteModel = [OrderALaCarteModel new];
  menuModel.delegate = self;
  carteModel.delegate = self;
  menuTableView.dataSource = menuModel;
  menuTableView.delegate = menuModel;
  carteTableView.dataSource = carteModel;
  carteTableView.delegate = carteModel;
  
  [menuButton setTitle:NSLocalizedString(@"Menus", @"").uppercaseString forState:UIControlStateNormal];
  [alacarteButton setTitle:NSLocalizedString(@"A la carte", @"").uppercaseString forState:UIControlStateNormal];
  [reviewButton setTitle:NSLocalizedString(@"Order", @"").uppercaseString forState:UIControlStateNormal];
  
  [self buttonClicked:menuButton];
}

- (void)didReceiveMemoryWarning {
  [super didReceiveMemoryWarning];
  // Dispose of any resources that can be recreated.
}

#pragma mark - Menu and ALaCarte delegate methods

- (void)menuCompositionClicked:(MenuComposition *)composition {
  CommandMenuViewController * controller = [[CommandMenuViewController alloc] initWithNibName:@"CommandMenuView" bundle:nil];

  controller.composition = composition;
  presentController = controller;

  CGRect frame = presentController.view.frame;
  frame.origin.x += frame.size.width;
  presentController.view.frame = frame;
  
  [self.view addSubview:presentController.view];
  [UIView animateWithDuration:0.5 animations:^{
    CGRect frame = presentController.view.frame;
    frame.origin.x -= frame.size.width;
    presentController.view.frame = frame;
  }];
}

- (void)dishClicked:(Dish *)dish {
  
}

#pragma mark - IBAction methods

- (IBAction)buttonClicked:(UIButton *)sender {
  for (UIView * v in clickedViews)
    v.hidden = v.tag != sender.tag;
  for (UIView * v in contentViews)
    v.hidden = v.tag != sender.tag;
}

/*
 #pragma mark - Navigation
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 */

@end
